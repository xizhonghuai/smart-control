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

function openToast(winId) {
    var $toast = $('#' + winId);
    $toast.fadeIn(100);
}

function closeToast(winId) {
    var $toast = $('#' + winId);
    $toast.fadeOut(100);
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
// const API_URL = "http://127.0.0.1:8000/"
const API_URL = "http://8.131.57.109:8000/"
const RES_OK = 20000;
const RES_ERROR = 40000;


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
        window.location.href = "./index.html?redirect_uri=" + encodeURI(uri);
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
                    var reg = new RegExp("({)" + i + "(})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
}

function getSessionStorage(key) {
    return JSON.parse(sessionStorage.getItem(key));
}

function setSessionStorage(key, value) {
    sessionStorage.setItem(key, JSON.stringify(value));
}

//字符串格式：2020-10-13 12:00:01
function stringToDate(strDate) {
    var tempStrs = strDate.split(" ");

    var dateStrs = tempStrs[0].split("-");
    var year = parseInt(dateStrs[0], 10);
    var month = parseInt(dateStrs[1], 10) - 1;
    var day = parseInt(dateStrs[2], 10);

    var timeStrs = tempStrs[1].split(":");
    var hour = parseInt(timeStrs [0], 10);
    var minute = parseInt(timeStrs[1], 10);
    var second = parseInt(timeStrs[2], 10);

    var date = new Date(year, month, day, hour, minute, second);
    return date;
}

function isDate(date) {
    return date instanceof Date && !isNaN(date.getTime())
}

function formatDate(date, format) {
    if (!date) return;
    if (!format) format = "HH:mm:ss";
    switch (typeof date) {
        case "string":
            date = new Date(date.replace(/-/, "/"));
            break;
        case "number":
            date = new Date(date);
            break;
    }
    if (!date instanceof Date) return;
    var dict = {
        "yyyy": date.getFullYear(),
        "M": date.getMonth() + 1,
        "d": date.getDate(),
        "H": date.getHours(),
        "m": date.getMinutes(),
        "s": date.getSeconds(),
        "MM": ("" + (date.getMonth() + 101)).substr(1),
        "dd": ("" + (date.getDate() + 100)).substr(1),
        "HH": ("" + (date.getHours() + 100)).substr(1),
        "mm": ("" + (date.getMinutes() + 100)).substr(1),
        "ss": ("" + (date.getSeconds() + 100)).substr(1)
    };
    return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function () {
        return dict[arguments[0]];
    });
}

function getDiffDay(date_1, date_2) {
    // 计算两个日期之间的差值
    let totalDays, diffDate;
    // 将两个日期都转换为毫秒格式，然后做差
    diffDate = date_1.getTime() - date_2.getTime(); // 取相差毫秒数的绝对值
    totalDays = Math.round(diffDate / (24 * 60 * 60 * 1000));
    return totalDays;   // 相差的天数
}

function timeAddSecond(date, second) {
    return new Date(date.getTime() + 1000 * second);
}

function createDates(date1, date2) {
    let dates = [date1];
    let temp = new Date(date1);
    let diffDay = getDiffDay(date2, date1);
    let oneDay = 24 * 60 * 60 * 1000;
    console.log(diffDay);
    for (let i = 1; i <= diffDay; i++) {
        dates.push(new Date(temp.getTime() + (i * oneDay)))
    }
    return dates;
}

function isUndefined(v) {
    return typeof (v) == "undefined";
}

function isEmpty(v) {
    return isUndefined(v) || (v === null);
}

function groupBy(array, f) {
    const groups = {};
    array.forEach(function (o) {
        const group = JSON.stringify(f(o));
        groups[group] = groups[group] || [];
        groups[group].push(o);
    });
    return Object.keys(groups).map(function (group) {
        return groups[group];
    });
}

function arrayObjectNonNull(array) {
    let ar = []
    array.forEach(function (item) {
        if (isEmpty(item)) {
        } else {
            ar.push(item);
        }
    });
    return ar;
}


