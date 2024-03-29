<!-- 正则验证 start-->
/**
 * 判空
 *
 * @param obj
 * @returns {boolean}
 */
function isNull(obj) {
    if (obj == null || obj == undefined || obj.trim() == "") {
        return true;
    }
    return false;
}

/**
 * 参数长度验证
 *
 * @param obj
 * @param lengthmodalLogin
 * @returns {boolean}
 */
function validLength(obj, length) {
    if (obj.trim().length < length) {
        return true;
    }
    return false;
}

/**
 * 用户名称验证 4到16位（字母，数字，下划线，减号）
 *
 * @param userName
 * @returns {boolean}
 */
function validUserName(userName) {
    var pattern = /^[a-zA-Z0-9_-]{4,16}$/;
    if (pattern.test(userName.trim())) {
        return (true);
    } else {
        return (false);
    }
}

/**
 * 用户密码验证 最少6位，最多20位字母或数字的组合
 *
 * @param password
 * @returns {boolean}
 */
function validPassword(password) {
    var pattern = /^[a-zA-Z0-9]{6,20}$/;
    if (pattern.test(password.trim())) {
        return (true);
    } else {
        return (false);
    }
}

<!-- 正则验证 end-->

function login() {
    var userName = $("#userName_login").val();
    var password = $("#password_login").val();
    if (isNull(userName)) {
        showErrorInfo("请输入用户名!");
        return;
    }
    if (!validUserName(userName)) {
        showErrorInfo("请输入正确的用户名!");
        return;
    }
    if (isNull(password)) {
        showErrorInfo("请输入密码!");
        return;
    }
    if (!validPassword(password)) {
        showErrorInfo("请输入正确的密码!");
        return;
    }
    var data = {"userName": userName, "password": password}
    $.ajax({
        type: "POST",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "fly/users/login",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function (result) {
            if (result.resultCode == 200) {
                $('.alert-danger').css("display", "none");
                setCookie("token", result.data.userToken);
                swal("登陆成功！", {icon: "success"});
                if (window.location.href.indexOf("login") > 0)
                    window.location.href = "/fly";
                else
                    window.location.reload();
            }
            ;
            if (result.resultCode == 500) {
                showErrorInfo("登陆失败!请检查账号和密码！");
                return;
            }
        },
        error: function () {
            $('.alert-danger').css("display", "none");
            showErrorInfo("接口异常，请联系管理员！");
            return;
        }
    });
}

function logout() {
    $.ajax({
        type: "GET",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "fly/users/logout",
        contentType: "application/json; charset=utf-8",
        success: function () {
            swal("注销成功！", {icon: "success"});
            window.location.href = "/fly";
        },
        error: function () {
            $('.alert-danger').css("display", "none");
            showErrorInfo("接口异常，请联系管理员！");
            return;
        }
    });
}

<!-- cookie操作 start-->

/**
 * 写入cookie
 *
 * @param name
 * @param value
 */
function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";path=/";

}

/**
 * 读取cookie
 * @param name
 * @returns {null}
 */
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

/**
 * 删除cookie
 * @param name
 */
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

/**
 * 检查cookie
 */
function checkCookie() {
    // if (getCookie("token") == null) {
    //     swal("请先登录！", {icon: "warning"});
    //     //window.location.href = "login.html";
    //     openModal('modalLogin');
    // }
}

/**
 * 检查cookie
 */
function checkResultCode(code) {
    if (code == 402) {
        //window.location.href = "login.html";
        openModal('modalLogin');
    }
}

<!-- cookie操作 end-->

// 展示错误信息
function showErrorInfo(info) {
    $('.alert-danger').css("display", "block");
    $('.alert-danger').html(info);
}

// 消除错误信息
function clearErrorInfo() {
    $('.alert-danger').css("display", "none");
    $('.alert-danger').html();
}

function openModal(target) {
    clearErrorInfo();
    var modal = new Custombox.modal({
        content: {
            target: '#' + target,
            effect: 'fadein'
        }
    });
    modal.open();
}


/**
 * 获取jqGrid选中的一条记录
 * @returns {*}
 */
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        swal("请先选择一条记录", {icon: "warning"});
        return;
    }
    var selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        swal("只能选择一条记录", {icon: "warning"});
        return;
    }
    return selectedIDs[0];
}

/**
 * 获取jqGrid选中的多条记录
 * @returns {*}
 */
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        swal("请至少选择一条记录", {icon: "warning"});
        return;
    }
    return grid.getGridParam("selarrrow");
}

/**
 * 延迟加载底部DOM
 */
function addFooter() {
    if ($("title").text() == "login")
        return;
    var footer = "<footer class=\"main-footer\">\n" +
        "    <strong>Copyright &copy; 2019 <a href=\"##\">freekingd.com</a>.</strong>\n" +
        "    All rights reserved.\n" +
        "    <div class=\"float-right d-none d-sm-inline-block\">\n" +
        "        <b>Version</b> 2.0\n" +
        "    </div>\n" +
        "</footer>";
    $("body").append(footer);
}
setTimeout('addFooter()', 100);