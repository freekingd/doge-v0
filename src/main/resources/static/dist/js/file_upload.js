$(function () {
    var guid = getGuid();
    $list = $('#fileList');
    var flie_count = 0;
    // 监听分块上传过程
    WebUploader.Uploader.register({
            "before-send": "beforeSend" //每个分片上传前
        },
        {//如果有分块上传，则每个分块上传之前调用此函数
            beforeSend: function (block) {
                // block为分块数据。
                // file为分块对应的file对象。
                var file = block.file;
                var fileName = file.name;
                var deferred = WebUploader.Deferred();
                $.ajax({
                    type: "POST",
                    url: "/upload/checkChunk",  //验证分片是否存在的请求
                    data: {
                        fileName: fileName,//文件名
                        guid: guid,
                        chunk: block.chunk,  //当前分块下标
                        chunks: block.chunks  //分块
                    },
                    cache: false,
                    async: false,
                    timeout: 2000,
                    success: function (response) {
                        if (response.resultCode == 200) {
                            //分块存在，跳过
                            console.log("已存在！跳过")
                            deferred.reject();
                        } else {
                            //分块不存在或不完整，重新发送该分块内容
                            console.log("不存在！上传分片")
                            deferred.resolve();
                        }
                    }
                });
                this.owner.options.formData.guid = guid;
                this.owner.options.formData.fileName_ = file.name;
                if (block.chunks > 1) { //文件大于chunksize分片上传
                    this.owner.options.formData.isChunked = true;
                } else {
                    this.owner.options.formData.isChunked = false;
                }
                return deferred.promise();
            }
        });
    var uploader = WebUploader.create({
        auto: false,// 选完文件后，是否自动上传
        swf: 'plugins/webupload/Uploader.swf',// swf文件路径
        server: '/upload/files',// 文件接收服务端url
        method: 'POST',// 服务端请求方法
        pick: '#picker',// 选择文件的按钮
        fileNumLimit: 10,//文件总数量只能选择10个,多于10个部分不会
        compress: false,//配置压缩的图片的选项。如果此选项为false, 则图片在上传前不进行压缩。
        chunked: true, //开启分块上传
        chunkSize: 1 * 1024 * 1024,//分片大小 默认5M
        chunkRetry: 3,//网络问题上传失败后重试次数
        threads: 1, //上传并发数 大文件时建议设置为1
        fileSizeLimit: 2000 * 1024 * 1024,//验证文件总大小是否超出限制, 超出则不允许加入队列 最大2000M
        fileSingleSizeLimit: 2000 * 1024 * 1024//验证单个文件大小是否超出限制, 超出则不允许加入队列  最大2000M
        //为了大文件处理就没有设置文件类型限制,可根据业务需求进行设置
        // accept: {//指定接受哪些类型的文件
        //     title: 'Images',
        //     extensions: 'gif,jpg,jpeg,bmp,png',// 只允许选择部分图片格式文件。
        //     mimeTypes: 'image/*'
        // },
    });
    // 当有文件被添加进队列的时候
    uploader.on('fileQueued', function (file) {
        $list.append('<div class=\"col-md-4\">' +
            '<div id=\"' + file.id + '\"class=\"card\">' +
            '<h5 class="info">' + file.name + '<button type="button" fileId="' + file.id + '" class="btn btn-danger btn-delete"><span class="glyphicon glyphicon-trash"></span></button></h5>' +
            '<input type="hidden" id="s_WU_FILE_' + flie_count + '" />' +
            '</div></div>');
        flie_count++;
        //删除要上传的文件
        //每次添加文件都给btn-delete绑定删除方法
        $(".btn-delete").click(function () {
            uploader.removeFile(uploader.getFile($(this).attr("fileId"), true));
            $(this).parent().parent().fadeOut();
            $(this).parent().parent().remove();
        });
    });
    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {
        var $li = $('#' + file.id),
            $percent = $li.find('.progress .progress-bar');
        // 避免重复创建
        if (!$percent.length) {
            $percent = $('<div class="progress progress-sm active">' +
                '<div class="progress-bar bg-success progress-bar-striped" role="progressbar" style="width: 0%">' +
                '</div>' +
                '</div>').appendTo($li).find('.progress-bar');
        }
        $li.find('p.state').text('上传中');
        $percent.css('width', percentage * 100 + '%');
    });

    uploader.on('uploadSuccess', function (file, res) {
        if (res.resultCode == 200) {
            $('#' + file.id).find('p.state').text('已上传');
            $('#' + file.id).find(".progress").find(".progress-bar").attr("class", "progress-bar progress-bar-success");
            $('#' + file.id).find(".info").find('.btn').fadeOut('slow')
            $('#stopUpload').fadeOut('slow');
            if (res.data != null) {
                $("#uploadResult").append('<p>' + res.data + '</p>');
            }
        }
    });
    uploader.on('uploadError', function (file) {
        $('#' + file.id).find('p.state').text('上传出错');
        //上传出错后进度条变红
        $('#' + file.id).find(".progress").find(".progress-bar").attr("class", "progress-bar progress-bar-danger");
        //添加重试按钮
        //为了防止重复添加重试按钮，做一个判断
        if ($('#' + file.id).find(".btn-retry").length < 1) {
            var btn = $('<button type="button" fileid="' + file.id + '" class="btn btn-success btn-retry"><span class="glyphicon glyphicon-refresh"></span></button>');
            $('#' + file.id).find(".info").append(btn);//.find(".btn-danger")
        }
        $(".btn-retry").click(function () {
            uploader.retry(uploader.getFile($(this).attr("fileId")));
        });
    });

    $("#startUpload").click(function () {
        uploader.upload();//上传
    });
    $("#stopUpload").click(function () {
        var status = $('#stopUpload').attr("status");
        if (status == "suspend" || !status) {
            $("#stopUpload").html("断点续传");
            $("#stopUpload").attr("status", "continuous");
            uploader.stop(true);
        } else {
            $("#stopUpload").html("暂停上传");
            $("#stopUpload").attr("status", "suspend");
            uploader.upload(uploader.getFiles("interrupt"));
        }
    });
    uploader.on('uploadAccept', function (file, response) {
        if (response._raw === '{"error":true}') {
            return false;
        }
    });
});

function getGuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}