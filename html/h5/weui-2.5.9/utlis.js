/*tips*/
// html template
/*<div role="alert" id="warnToast" style="display: none;">
    <div class="weui-mask_transparent"></div>
    <div class="weui-toast">
        <i class="weui-icon-warn weui-icon_toast"></i>
        <p class="weui-toast__content">获取链接失败</p>
    </div>
</div>*/
function toast(winId) {
    var $toast = $('#' + winId);
    $toast.fadeIn(100);
    setTimeout(function () {
        $toast.fadeOut(100);
    }, 2000);
}

//key(需要检索的键）
function getSearchString(key) {
    return getSearchStringV2(key, window.location.search)
}

function GetQueryString(name) {
    var reg = new RegExp("\\b" + name + "=([^&]*)");
    var r = location.href.match(reg);
    if (r != null) return decodeURIComponent(r[1]);
}

function getSearchStringV2(key, url) {
    var str = url;
    str = decodeURIComponent(str);
    str = str.substring(1, str.length); // 获取URL中?之后的字符（去掉第一位的问号）
    // 以&分隔字符串，获得类似name=xiaoli这样的元素数组
    var arr = str.split("&");
    var obj = {};
    // 将每一个数组元素以=分隔并赋给obj对象
    for (var i = 0; i < arr.length; i++) {
        var tmp_arr = arr[i].split("=");
        obj[decodeURIComponent(tmp_arr[0])] = decodeURIComponent(tmp_arr[1]);
    }
    return obj[key];
}

/*net*/
const API_URL = "http://8.131.57.109:8000/"
const RES_OK = 20000;
const RES_ERROR = 40000;

// const API_URL = "http://127.0.0.1:8000/"

function getLocalToken() {
    return localStorage.getItem("access_token");
}

function httpGet(api) {
    var result;
    var settings = {
        "url": API_URL + api,
        "method": "GET",
        "timeout": 0,
        async: false,
        "headers": {
            "token": getLocalToken()
        },
    };
    $.ajax(settings).done(function (response) {
        /*     console.log(response);*/
        result = response;
    });
    return result;
}

function httpRequest(api, method, body) {
    var result;
    var settings = {
        "url": API_URL + api,
        "method": method,
        "timeout": 0,
        async: false,
        "headers": {
            "token": getLocalToken(),
            "Content-Type": "application/json;charset=UTF-8"
        },
        "data": JSON.stringify(body)
    };
    $.ajax(settings).done(function (response) {
        console.log(response);
        result = response;
    });
    return result;
}

function verifyJump(uri) {
    let response = httpGet("api/v1/account/verify");
    if (response.code === 40001) {
        if (window.location.href.includes("index.html")) {
            return;
        }
        window.location.href = "./index.html?redirect_uri=" + uri;
    }
}

function checkToken() {
    let response = httpGet("api/v1/account/verify");
    return response.code === 20000;
}

String.prototype.format = function (args) {
    var result = this;
    if (arguments.length > 0) {
        if (arguments.length == 1 && typeof (args) == "object") {
            for (var key in args) {
                if (args[key] != undefined) {
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        } else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                    //var reg = new RegExp("({[" + i + "]})", "g");//这个在索引大于9时会有问题，谢谢何以笙箫的指出
                    var reg = new RegExp("({)" + i + "(})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
}

