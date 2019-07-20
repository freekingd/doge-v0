var uploader;
$(function () {
    //隐藏弹框
    $('#pictureModal').modal('hide');
    //隐藏错误提示框
    $('.alert-danger').css("display", "none");

    //modal绑定hide事件
    $('#pictureModal').on('hide.bs.modal', function () {
        reset();
    })
    $("#jqGrid").jqGrid({
        url: 'fly/pictures/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', width: 50, sortable: false, hidden: true, key: true},
            {label: '图片预览', name: 'path', index: 'path', sortable: false, width: 105, formatter: imgFormatter},
            {label: '图片备注', name: 'remark', index: 'remark', sortable: false, width: 105},
            {label: '添加时间', name: 'createTime', index: 'createTime', sortable: true, width: 80}
        ],
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    function imgFormatter(cellvalue) {
        return "<a href='" + cellvalue + "'> <img src='" + cellvalue + "' height=\"120\" width=\"135\" alt='Fly Doge'/></a>";
    }

    new AjaxUpload('#upload', {
        action: 'fly/image',
        name: 'file',
        autoSubmit: true,
        responseType: "json",
        onSubmit: function (file, extension) {
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))) {
                swal('只支持jpg、png、gif格式的图片！', {
                    icon: "error"
                });
                return false;
            }
        },
        onComplete: function (file, r) {
            if (r.resultCode == 200) {
                swal("上传成功！", {icon: "success"});
                $("#picturePath").val(r.data);
                $("#img").attr("src", r.data);
                $("#img").attr("style", "width: 100px;height: 100px;display:block;");
                return false;
            } else {
                swal(r.message, {icon: "warning"})
            }
        }
    });


});

// 初始化webuploader
function initWebuploader() {
    var $list = $("#previewList");
    var pictures = [];
    var thumbnailWidth = 100;   //缩略图高度和宽度，当宽高度是0~1的时候，按照百分比计算
    var thumbnailHeight = 100;
    uploader = WebUploader.create({
        auto: false,// 选完文件后，是否自动上传
        swf: '/fly/plugins/webupload/Uploader.swf',// swf文件路径
        server: 'fly/image',// 文件接收服务端url
        method: 'POST',// 服务端请求方法
        pick: '#fileUploader',// 选择文件的按钮
        fileNumLimit: 10,//文件总数量只能选择10个,多于10个部分不会加入上传队列
        fileSizeLimit: 100 * 1024 * 1024,//验证文件总大小是否超出限制, 超出则不允许加入队列 100M
        fileSingleSizeLimit: 4 * 1024 * 1024,//验证单个文件大小是否超出限制, 超出则不允许加入队列 4M
        compress: false,//配置压缩的图片的选项。如果此选项为false, 则图片在上传前不进行压缩。
        threads: 4,//上传并发数,允许同时最大上传进程数,默认值为3
        accept: {//指定接受哪些类型的文件
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',// 只允许选择部分图片格式文件，可自行修改
            mimeTypes: 'image/*'
        },
    });
    //webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件
    uploader.on('fileQueued', function (file) {
        var $li = $(
            '<div class=\"col-md-4\">' +
            '<div id=\"' + file.id + '\"class=\"card\">' +
            '<div class=\"card-header\"' +
            '<h6 class=\"card-title text-danger\">' + file.name + '</h6>' +
            '</div>' +
            '<div class=\"card-body text-center\">' +
            '<span class=\"mailbox-attachment-icon has-img\">' +
            '<img>' +
            '</span></div></div></div>'
            ),
            $img = $li.find('img');
        $list.append($li);
        // 创建缩略图
        // 如果为非图片文件，可以不用调用此方法。
        uploader.makeThumb(file, function (error, src) {
            //webuploader方法
            if (error) {
                $img.replaceWith('<span>不能预览</span>');
                return;
            }
            $img.attr('src', src);
        }, thumbnailWidth, thumbnailHeight);
    });
    // 文件上传成功，标记上传成功。
    uploader.on('uploadSuccess', function (file, result) {
        if (result.resultCode == 200) {
            $('#' + file.id).append(' <div class="card-footer"><h6 class=\"card-title text-success\">上传成功</h6></div>');
            $("#imgResult").append('<p>' + result.data + '</p>');
            // 创建picture
            var picture = {"path": result.data, "remark": "批量添加"};
            pictures.push(picture);
        }
    });
    // 文件上传失败，显示上传出错。
    uploader.on('uploadError', function (file) {
        var $li = $('#' + file.id);
        $li.append(' <div class="card-footer"><h6 class=\"card-title text-danger\">上传失败</h6></div>');
    });
    // 所有文件上传完成时触发
    uploader.on('uploadFinished', function () {
        savePictures(pictures);
    });
    // 点击上传
    $("#uploadBatch").click(function () {
            uploader.upload();
        }
    );
}

// 批量保存图片
function savePictures(pictures) {
    $.ajax({
        type: 'POST',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "fly/pictures/saveBatch",//url
        contentType: "application/json; charset=utf-8",
        beforeSend: function (request) {
            //设置header值
            request.setRequestHeader("token", getCookie("token"));
        },
        data: JSON.stringify(pictures),
        success: function (result) {
            checkResultCode(result.resultCode);
            if (result.resultCode == 200) {
                swal("保存成功", {icon: "success"});
                Custombox.modal.closeAll();
                reload();
            }
            else {
                swal("保存失败", {icon: "warning"});
                Custombox.modal.closeAll();
            }
            ;
        },
        error: function () {
            swal("操作失败", {icon: "warning"});
        }
    });
}


//绑定modal上的保存按钮
$('#saveButton').click(function () {
    //验证数据
    if (validObject()) {
        //一切正常后发送网络请求
        //ajax
        var id = $("#pictureId").val();
        var picturePath = $("#picturePath").val();
        var pictureRemark = $("#pictureRemark").val();
        var data = {"path": picturePath, "remark": pictureRemark};
        var url = 'fly/pictures/save';
        //id>0表示编辑操作
        if (id > 0) {
            data = {"id": id, "path": picturePath, "remark": pictureRemark};
            url = 'fly/pictures/update';
        }
        $.ajax({
            type: 'POST',//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: url,//url
            contentType: "application/json; charset=utf-8",
            beforeSend: function (request) {
                //设置header值
                request.setRequestHeader("token", getCookie("token"));
            },
            data: JSON.stringify(data),
            success: function (result) {
                checkResultCode(result.resultCode);
                if (result.resultCode == 200) {
                    $('#pictureModal').modal('hide');
                    swal("保存成功", {icon: "success"});
                    reload();
                }
                else {
                    $('#pictureModal').modal('hide');
                    swal("保存失败", {icon: "warning"});
                }
                ;
            },
            error: function () {
                swal("操作失败", {icon: "warning"});
            }
        });

    }
});

function pictureAdd() {
    reset();
    $('.modal-title').html('图片添加');
    $('#pictureModal').modal('show');
}

// 批量添加图片
function pictureAddBatch() {
    initWebuploader();
    var modal = new Custombox.modal({
        content : {
            target: '#addBatchModal', //打开哪个弹框
            effect: 'fadein', //动画效果，效果很多可自行选择想要使用的
            id: null, //id
            close: true, // 是否允许使用esc键关闭弹窗 默认true
            animateFrom: 'top', // 动画出现位置 top、left、center、right
            animateTo: 'top', //动画消失位置 top、left、center、right
            positionX: 'left', //横坐标位置 top、left、center、right
            positionY: 'center', //纵坐标位置 top、left、center、right
            width: null, //宽度
            speedIn: 300, //进入速度，以毫秒为单位
            speedOut: 300, //消失速度，以毫秒为单位
            delay: 150, //动画延迟，以毫秒为单位
            fullscreen: false, //设置全屏模式，默认false
            onOpen: null, //钩子函数,弹窗打开时触发
            onComplete: null, //钩子函数,弹窗加载完成时触发
            onClose: function () {
                uploader.destroy();
                $("#previewList").html("");
            }, //钩子函数,弹窗关闭时触发
        }
    });
    modal.open();
}

function pictureEdit() {
    reset();
    $('.modal-title').html('图片编辑');

    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    //请求数据
    $.ajax({
        type: "GET",
        url: "fly/pictures/info/" + id,
        contentType: "application/json",
        beforeSend: function (request) {
            //设置header值
            request.setRequestHeader("token", getCookie("token"));
        },
        success: function (r) {
            checkResultCode(r.resultCode);
            if (r.resultCode == 200 && r.data != null) {
                //填充数据至modal
                $('#pictureId').val(r.data.id);
                $('#picturePath').val(r.data.path);
                $('#pictureRemark').val(r.data.remark);
                $("#img").attr("src", r.data.path);
                $("#img").attr("style", "width: 100px;height: 100px;display:block;");
            }
        }
    });
    //显示modal
    $('#pictureModal').modal('show');
}

/**
 * 数据验证
 */
function validObject() {
    var picturePath = $('#picturePath').val();
    if (isNull(picturePath)) {
        showErrorInfo("图片不能为空!");
        return false;
    }
    var pictureRemark = $('#pictureRemark').val();
    if (isNull(pictureRemark)) {
        showErrorInfo("备注信息不能为空!");
        return false;
    }
    if (!validLength(pictureRemark, 150)) {
        showErrorInfo("备注信息长度不能大于150!");
        return false;
    }
    if (!validLength(picturePath, 120)) {
        showErrorInfo("图片上传有误!");
        return false;
    }
    return true;
}

/**
 * 重置
 */
function reset() {
    //隐藏错误提示框
    $('.alert-danger').css("display", "none");
    //清空数据
    $('#pictureId').val(0);
    $('#picturePath').val('');
    $('#pictureRemark').val('');
    $("#img").attr("style", "display:none;");
}

function deletePicture() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    $.ajax({
        type: "POST",
        url: "fly/pictures/deleteBatch",
        contentType: "application/json",
        beforeSend: function (request) {
            //设置header值
            request.setRequestHeader("token", getCookie("token"));
        },
        data: JSON.stringify(ids),
        success: function (r) {
            checkResultCode(r.resultCode);
            if (r.resultCode == 200) {
                swal("删除成功！", {icon: "success"});
                $("#jqGrid").trigger("reloadGrid");
            } else {
                swal(r.message, {icon: "warning"});
            }
        }
    });
}

/**
 * jqGruploadid重新加载
 */
function reload() {
    reset();
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}