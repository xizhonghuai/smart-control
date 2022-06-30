!function (t, e) {
    "object" == typeof exports && "object" == typeof module ? module.exports = e() : "function" == typeof define && define.amd ? define([], e) : "object" == typeof exports ? exports.WAH = e() : t.WAH = e()
}(self, (function () {
    return (() => {
        "use strict";
        var t = {
            335: (t, e, n) => {
                n.d(e, {Z: () => o});
                var r = n(645), i = n.n(r)()((function (t) {
                    return t[1]
                }));
                i.push([t.id, "/* 控制器 */\n.wah-controls {\n  position: fixed;\n  z-index: 100000;\n  bottom: 50px;\n  right: constant(safe-area-inset-right);\n  right: env(safe-area-inset-right);\n  display: flex;\n  align-items: center;\n  background: rgba(0, 0, 0, 0.5);\n  color: #fff;\n  font-size: 14px;\n  border-top-left-radius: 4px;\n  border-bottom-left-radius: 4px;\n  overflow: hidden;\n}\n.wah-control {\n  padding: 4px;\n  display: flex;\n  align-items: center;\n}\n\n/* 热区蓝字 */\n.wah-hotarea-size {\n  z-index: 99999;\n  font-size: 10px;\n  background: rgba(0, 0, 255, 0.5);\n  color: #FFF;\n}\n\n/* 读屏红框 */\n.wah-reading-panel {\n  position: absolute;\n  top: 0;\n  right: 0;\n  bottom: 0;\n  left: 0;\n  pointer-events: none;\n}\n.wah-reading-outline {\n  position: absolute;\n  z-index: 99999;\n  transform: translateZ(999999px);\n  border: 2px solid rgba(255, 0, 0, 0.5);\n  background: rgba(100, 0, 0, 0.3);\n  font-size: 10px;\n  font-weight: 500;\n  line-height: 1.4;\n  color: #fff;\n  word-break: break-all;\n}\n", ""]);
                const o = i
            }, 645: t => {
                t.exports = function (t) {
                    var e = [];
                    return e.toString = function () {
                        return this.map((function (e) {
                            var n = t(e);
                            return e[2] ? "@media ".concat(e[2], " {").concat(n, "}") : n
                        })).join("")
                    }, e.i = function (t, n, r) {
                        "string" == typeof t && (t = [[null, t, ""]]);
                        var i = {};
                        if (r) for (var o = 0; o < this.length; o++) {
                            var a = this[o][0];
                            null != a && (i[a] = !0)
                        }
                        for (var l = 0; l < t.length; l++) {
                            var c = [].concat(t[l]);
                            r && i[c[0]] || (n && (c[2] ? c[2] = "".concat(n, " and ").concat(c[2]) : c[2] = n), e.push(c))
                        }
                    }, e
                }
            }, 695: t => {
                var e = {};
                t.exports = function (t) {
                    if (void 0 === e[t]) {
                        var n = document.querySelector(t);
                        if (window.HTMLIFrameElement && n instanceof window.HTMLIFrameElement) try {
                            n = n.contentDocument.head
                        } catch (t) {
                            n = null
                        }
                        e[t] = n
                    }
                    return e[t]
                }
            }, 379: t => {
                var e = [];

                function n(t) {
                    for (var n = -1, r = 0; r < e.length; r++) if (e[r].identifier === t) {
                        n = r;
                        break
                    }
                    return n
                }

                function r(t, r) {
                    for (var o = {}, a = [], l = 0; l < t.length; l++) {
                        var c = t[l], u = r.base ? c[0] + r.base : c[0], s = o[u] || 0, d = "".concat(u, " ").concat(s);
                        o[u] = s + 1;
                        var f = n(d), p = {css: c[1], media: c[2], sourceMap: c[3]};
                        -1 !== f ? (e[f].references++, e[f].updater(p)) : e.push({
                            identifier: d,
                            updater: i(p, r),
                            references: 1
                        }), a.push(d)
                    }
                    return a
                }

                function i(t, e) {
                    var n = e.domAPI(e);
                    return n.update(t), function (e) {
                        if (e) {
                            if (e.css === t.css && e.media === t.media && e.sourceMap === t.sourceMap) return;
                            n.update(t = e)
                        } else n.remove()
                    }
                }

                t.exports = function (t, i) {
                    var o = r(t = t || [], i = i || {});
                    return function (t) {
                        t = t || [];
                        for (var a = 0; a < o.length; a++) {
                            var l = n(o[a]);
                            e[l].references--
                        }
                        for (var c = r(t, i), u = 0; u < o.length; u++) {
                            var s = n(o[u]);
                            0 === e[s].references && (e[s].updater(), e.splice(s, 1))
                        }
                        o = c
                    }
                }
            }, 216: t => {
                t.exports = function (t) {
                    var e = document.createElement("style");
                    return t.setAttributes(e, t.attributes), t.insert(e), e
                }
            }, 795: t => {
                t.exports = function (t) {
                    var e = t.insertStyleElement(t);
                    return {
                        update: function (n) {
                            !function (t, e, n) {
                                var r = n.css, i = n.media, o = n.sourceMap;
                                i ? t.setAttribute("media", i) : t.removeAttribute("media"), o && "undefined" != typeof btoa && (r += "\n/*# sourceMappingURL=data:application/json;base64,".concat(btoa(unescape(encodeURIComponent(JSON.stringify(o)))), " */")), e.styleTagTransform(r, t)
                            }(e, t, n)
                        }, remove: function () {
                            !function (t) {
                                if (null === t.parentNode) return !1;
                                t.parentNode.removeChild(t)
                            }(e)
                        }
                    }
                }
            }
        }, e = {};

        function n(r) {
            var i = e[r];
            if (void 0 !== i) return i.exports;
            var o = e[r] = {id: r, exports: {}};
            return t[r](o, o.exports, n), o.exports
        }

        n.n = t => {
            var e = t && t.__esModule ? () => t.default : () => t;
            return n.d(e, {a: e}), e
        }, n.d = (t, e) => {
            for (var r in e) n.o(e, r) && !n.o(t, r) && Object.defineProperty(t, r, {enumerable: !0, get: e[r]})
        }, n.o = (t, e) => Object.prototype.hasOwnProperty.call(t, e), n.r = t => {
            "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(t, Symbol.toStringTag, {value: "Module"}), Object.defineProperty(t, "__esModule", {value: !0})
        };
        var r = {};
        return (() => {
            n.r(r), n.d(r, {default: () => Z});
            var t = n(379), e = n.n(t), i = n(795), o = n.n(i), a = n(695), l = n.n(a), c = n(216), u = n.n(c),
                s = n(335), d = {
                    styleTagTransform: function (t, e) {
                        if (e.styleSheet) e.styleSheet.cssText = t; else {
                            for (; e.firstChild;) e.removeChild(e.firstChild);
                            e.appendChild(document.createTextNode(t))
                        }
                    }, setAttributes: function (t) {
                        var e = n.nc;
                        e && t.setAttribute("nonce", e)
                    }, insert: function (t) {
                        var e = l()("head");
                        if (!e) throw new Error("Couldn't find a style target. This probably means that the value for the 'insert' parameter is invalid.");
                        e.appendChild(t)
                    }
                };
            d.domAPI = o(), d.insertStyleElement = u(), e()(s.Z, d), s.Z && s.Z.locals && s.Z.locals;
            let f = [];
            const p = {
                proxy: t => new Proxy(t, {
                    get(t, e) {
                        if ("function" == typeof t[e]) return t[e].bind(t);
                        const n = f.find((e => e.obj === t));
                        if (n) {
                            if (n.props.indexOf(e) > -1) return null;
                            n.props.push(e)
                        } else f.push({obj: t, props: [e]});
                        return Reflect.get(t, e)
                    }
                }), clear() {
                    f = []
                }
            }, h = function t(e, n) {
                if (!e) return "";
                n || (n = "name");
                const r = [];
                return e.wordings && ("all" === n ? r.push(Object.values(e.wordings).map((t => t.join(""))).filter((t => t)).join("，")) : r.push(e.wordings[n].filter((t => t)))), e.children && e.children.forEach((e => {
                    r.push(t(e, n))
                })), r.join("all" === n ? "，" : "")
            };

            function g(t, e) {
                try {
                    return t.style && t.style[e] || window.getComputedStyle(t, null).getPropertyValue(e.replace(/([A-Z])/g, "-$1").toLowerCase())
                } catch (t) {
                    return ""
                }
            }

            function m(t) {
                if (!t) return "absolute";
                let e = t;
                for (; ;) {
                    const n = g(e, "position");
                    if ("fixed" === n) return "fixed";
                    if ("sticky" === n) return e === t ? "sticky" : "absolute";
                    if (e = e.parentElement, e === document.body || !e) break
                }
                return "absolute"
            }

            const b = function (t, e, n) {
                return t = (t || "").trim().replace(/\n/g, ""), e = (e || "").trim().replace(/\n/g, ""), t === (n = (n || "").trim().replace(/\n/g, "")) && (n = ""), t || (t = n, n = ""), {
                    name: [t],
                    type: [e],
                    desc: [n]
                }
            }, y = function (t, e) {
                if (t.id) {
                    const e = document.querySelector("label[for=" + t.id + "]");
                    if (e) return e.getAttribute("aria-label") || e.textContent || e.title || ""
                }
                return ""
            }, w = function (t, e) {
                if (t.getAttribute) {
                    const n = t.getAttribute("aria-labelledby");
                    if (n) {
                        const t = document.getElementById(n);
                        if (t) return h(O(t, e, {notScanRefElement: !0}))
                    }
                }
                return t.getAttribute("aria-label") || ""
            }, x = function (t, e) {
                if (t.getAttribute) {
                    const n = t.getAttribute("aria-describedby");
                    if (n) {
                        const t = document.getElementById(n);
                        if (t) return h(O(t, e, {notScanRefElement: !0}), "desc")
                    }
                }
                return ""
            }, v = {h1: "标题级别一", h2: "标题级别二", h3: "标题级别三", h4: "标题级别四", h5: "标题级别五", h6: "标题级别六"}, A = function (t) {
                return function (e, ...n) {
                    const r = p.proxy(e), i = t.apply(null, [r, e].concat(n));
                    return p.clear(), i
                }
            }, C = {
                a: A((function (t, e, n) {
                    return b(w(e, n) || t.textContent || t.title, e.href && "链接", x(e, n) || t.title)
                })), button: A((function (t, e, n) {
                    return function (t, e, n, r) {
                        return b(w(e, n) || t.textContent || t.title, "按钮", x(e, n) || t.title)
                    }(t, e, n)
                })), img: A((function (t, e, n) {
                    return b(w(e, n) || t.alt || t.title, "图像", x(e, n) || t.title)
                })), svg: A((function (t, e, n) {
                    return b(w(e, n) || t.alt || t.title, "图像", x(e, n) || t.title)
                })), input: A((function (t, e, n) {
                    switch (e.type) {
                        case"image":
                            return b(y(e) || w(e, n) || t.alt || t.value || t.title, "图像", x(e, n) || t.title);
                        case"button":
                            return b(y(e) || w(e, n) || t.value || t.title, "按钮", x(e, n) || t.value || t.title);
                        case"submit":
                            return b(y(e) || w(e, n) || t.value || "提交", "按钮", x(e, n) || t.title);
                        case"reset":
                            return b(y(e) || w(e, n) || t.value || "重置", "按钮", x(e, n) || t.title);
                        case"checkbox":
                            return b(y(e) || w(e, n), "复选框，" + (e.checked ? "已勾选" : "未勾选"), x(e, n) || t.title);
                        case"radio":
                            return b(y(e) || w(e, n), "单选按钮，" + (e.checked ? "已勾选" : "未勾选"), x(e, n) || t.title);
                        case"range":
                            let r = e.getAttribute("aria-valuetext");
                            if (!r) {
                                const t = +e.min || 0, n = +e.max || 100, i = +e.value || (t + n) / 2;
                                r = i + "，" + (100 * (i - t) / (n - t)).toFixed(1) + "%"
                            }
                            return b([y(e) || w(e, n) || t.title, r].filter((t => t)).join("，"), "可调", x(e, n) || t.title);
                        default:
                            return b((y(e) || w(e, n) || "") + (t.value || t.placeholder || t.title), "文本栏", x(e, n) || t.placeholder || t.title)
                    }
                })), textarea: A((function (t, e, n) {
                    return b((y(e) || w(e, n) || "") + (t.value || t.placeholder || t.title), "多行文本栏", x(e, n) || t.placeholder || t.title)
                })), label: A((function (t, e, n) {
                    return b(h(O(e, n), "all") || t.textContent || w(e, n), "", "")
                })), progress: A((function (t, e, n) {
                    let r = e.getAttribute("aria-valuetext");
                    if (!r) {
                        const t = 0, n = +e.max || 100, i = +e.value || t;
                        r = i + "，" + (100 * (i - t) / (n - t)).toFixed(1) + "%"
                    }
                    return b([y(e) || w(e, n) || t.title, r].filter((t => t)).join("，"), "", x(e, n) || t.title)
                }))
            }, E = function (t, e, n, r) {
                return b(w(e, n) || h(O(e, n)) || t.title, r, x(e, n) || t.title)
            }, j = {
                img: A((function (t, e, n) {
                    return b(w(e, n) || t.alt || t.title, "图像", x(e, n) || t.title)
                })), button: A((function (t, e, n) {
                    return E(t, e, n, "按钮")
                })), option: A((function (t, e, n) {
                    return E(t, e, n, "")
                })), banner: A((function (t, e, n) {
                    return b(w(e, n) || t.title || h(O(e, n)), "横幅", "")
                })), textbox: A((function (t, e, n) {
                    return b(t.textContent, "文本栏")
                })), text: A((function (t, e, n) {
                    return b(h(O(e, n)))
                })), checkbox: A((function (t, e, n) {
                    return b(y(e) || w(e, n), "复选框，" + ("true" === e.ariaChecked ? "已勾选" : "未勾选"), x(e, n) || t.title)
                })), radio: A((function (t, e, n) {
                    return b(y(e) || w(e, n) || h(O(e, n)), "单选按钮，" + ("true" === e.ariaChecked ? "已勾选" : "未勾选"), x(e, n) || t.title)
                })), slider: A((function (t, e, n) {
                    let r = e.getAttribute("aria-valuetext");
                    if (!r) {
                        const t = +e.getAttribute("aria-valuemin") || 0, n = +e.getAttribute("aria-valuemax") || 100,
                            i = +e.getAttribute("aria-valuenow") || (t + n) / 2;
                        r = i + "，" + (100 * (i - t) / (n - t)).toFixed(1) + "%"
                    }
                    return b([y(e) || w(e, n) || t.title, r].filter((t => t)).join("，"), "可调", x(e, n) || t.title)
                })), progressbar: A((function (t, e, n) {
                    let r = e.getAttribute("aria-valuetext");
                    if (!r) {
                        const t = +e.getAttribute("aria-valuemin") || 0, n = +e.getAttribute("aria-valuemax") || 100,
                            i = +e.getAttribute("aria-valuenow") || (t + n) / 2;
                        r = i + "，" + (100 * (i - t) / (n - t)).toFixed(1) + "%"
                    }
                    return b([y(e) || w(e, n) || t.title, r].filter((t => t)).join("，"), "", x(e, n) || t.title)
                }))
            }, k = {
                link: A((function (t, e, n) {
                    return b(w(e, n) || t.title, "链接", x(e, n) || t.title)
                })), navigation: A((function (t, e, n) {
                    return b(w(e, n) || t.title, "导航", x(e, n) || t.title)
                }))
            }, S = {
                alertdialog: A((function (t, e, n) {
                    return b(w(e, n) || t.title, "网页警告对话框", x(e, n) || t.title)
                })), dialog: A((function (t, e, n) {
                    return b(w(e, n) || t.title, "网页对话框", x(e, n) || t.title)
                }))
            }, T = "text", L = "role", I = ["script", "style"];

            function O(t, e, n) {
                const r = {parent: e, dom: t, inheritARIAType: v[t.nodeName.toLowerCase()] || ""};
                if (n || (n = {}), n.notScanRefElement && (t.getAttribute("aria-labelledby") || t.getAttribute("aria-describedby"))) return null;
                const i = t.getBoundingClientRect(), o = [], a = [];
                let l = r;
                for (; l.inheritARIAType && a.push(l.inheritARIAType), l = l.parent, l;) ;
                return t.childNodes.forEach((t => {
                    if (n.notScanRefElement && t.getAttribute && (t.getAttribute("aria-labelledby") || t.getAttribute("aria-describedby"))) return;
                    const e = t.nodeName.toLowerCase();
                    if (!(t.getAttribute && void 0 !== t.getAttribute("aria-hidden") && null !== t.getAttribute("aria-hidden") || "none" === g(t, "display")) && (!/^#/.test(e) || /^#text/.test(e)) && !(I.indexOf(e) > -1)) if ("#text" === e) {
                        const n = t.textContent.trim();
                        if (n) {
                            const t = b(n);
                            a.length && t.type.unshift(a.join("，")), o.push({type: T, name: e, wordings: t})
                        }
                    } else {
                        const i = t.getBoundingClientRect(), l = (t.getAttribute("role") || "").toLowerCase();
                        if (l) if (k[l] || S[l]) {
                            const e = Object.assign({}, r);
                            let n;
                            k[l] ? e.inheritARIAType = k[l](t, e).type[0] : n = S[l](t, e), a.length && n.type.unshift(a.join("，"));
                            const c = O(t, e);
                            o.push({type: L, node: t, name: l, rect: i, wordings: n, children: c ? [c] : []})
                        } else if (j[l]) {
                            const e = j[l](t, r);
                            a.length && e.type.unshift(a.join("，")), o.push({
                                type: L,
                                node: t,
                                name: l,
                                rect: i,
                                wordings: e
                            })
                        } else {
                            const e = O(t, r, n);
                            e && o.push(e)
                        } else if (C[e]) {
                            const n = C[e](t, r);
                            a.length && n.type.unshift(a.join("，")), o.push({
                                type: "replaced",
                                node: t,
                                name: e,
                                rect: i,
                                wordings: n
                            })
                        } else {
                            const e = O(t, r, n);
                            e && o.push(e)
                        }
                    }
                })), o.length ? {type: "tree", node: t, name: t.nodeName, rect: i, children: o} : null
            }

            function R(t, e) {
                if (!e) return;
                const n = function (t) {
                    if (!t.rect) return;
                    const e = window.scrollY || document.documentElement.scrollTop,
                        n = window.scrollX || document.documentElement.scrollLeft, r = document.createElement("DIV"),
                        i = t.rect, o = m(t.node), a = "fixed" === o;
                    if (r.setAttribute("aria-hidden", "true"), r.classList.add("wah-reading-outline"), r.style.position = o, r.style.top = (a ? 0 : e) + i.top - 1 + "px", r.style.left = (a ? 0 : n) + i.left - 1 + "px", r.style.width = i.width - 2 + "px", r.style.height = i.height - 2 + "px", r._wahNode = t.node, t.wordings) r.innerText = t.wordings.name.concat(t.wordings.type.concat(t.wordings.desc)).filter((t => t && t)).join("，"); else {
                        if (!t.children) return;
                        if (!t.children.filter((t => t.type === T)).length) return;
                        r.innerText = t.children.map((t => {
                            switch (t.type) {
                                case T:
                                    return t.wordings.name.concat(t.wordings.type.concat(t.wordings.desc)).filter((t => t && t)).join("，");
                                default:
                                    return "<" + t.name + ">"
                            }
                        })).join(" ")
                    }
                    return r
                }(e);
                n && t.appendChild(n), e.children && e.children.length && e.children.forEach((e => {
                    R(t, e)
                }))
            }

            const M = {
                    hide() {
                        const t = document.querySelector("#wah-reading-panel");
                        t && t.parentElement.removeChild(t), p.clear()
                    }, show(t) {
                        t || (t = document.body);
                        const e = O(t);
                        e && function (t, e) {
                            if (!e) return;
                            const n = document.createElement("DIV");
                            n.id = "wah-reading-panel", n.classList.add("wah-reading-panel"), n.setAttribute("aria-hidden", "true"), R(n, e), t.appendChild(n)
                        }(t, e)
                    }
                },
                N = ["a:not([wah-hotarea-scaned])", "input:not([wah-hotarea-scaned])", "button:not([wah-hotarea-scaned])"],
                F = [];
            let P;

            function z(t) {
                if (!t || t === document) return;
                const e = document.createElement("DIV"), n = window.pageYOffset || document.documentElement.scrollTop,
                    r = window.pageXOffset || document.documentElement.scrollLeft, i = t.getBoundingClientRect(),
                    o = m(t), a = "fixed" === o;
                e.classList && e.classList.add("wah-hotarea-size"), e.textContent = "[" + t.offsetWidth + "*" + t.offsetHeight + "]", e.style.position = o, e.style.top = (a ? 0 : n) + i.top + "px", e.style.left = (a ? 0 : r) + i.left + "px", e._wahNode = t, t.getAttribute && !t.getAttribute("wah-hotarea") && t.setAttribute("wah-hotarea", "click"), document.querySelector("#wah-hotarea-panel").appendChild(e)
            }

            !function () {
                const t = Object.getOwnPropertyNames(window).filter((function (t) {
                    return /^HTML/.test(t)
                })).map((function (t) {
                    return window[t]
                }));
                for (let e = 0; e < t.length; e++) !function (n) {
                    t[e].prototype.addEventListener = function (t, e, r) {
                        return "click" !== t && "touchstart" !== t && "touchend" !== t || (this && this.setAttribute && this.setAttribute("wah-hotarea", t), F.indexOf(this) < 0 && F.push(this)), n.apply(this, arguments)
                    }
                }(t[e].prototype.addEventListener)
            }();
            const H = {
                hide() {
                    P && document.head.removeChild(P);
                    const t = document.querySelector("#wah-hotarea-panel");
                    t && t.parentElement.removeChild(t)
                }, show() {
                    const t = document.createElement("DIV");
                    t.id = "wah-hotarea-panel", document.body.appendChild(t);
                    const e = document.querySelectorAll(N);
                    Array.prototype.forEach.call(e, (function (t) {
                        t.setAttribute("wah-hotarea-scaned", !0), F.indexOf(t) < 0 && F.push(t)
                    })), F.forEach(z), P = document.createElement("style"), P.appendChild(document.createTextNode("[wah-hotarea] {\n  box-shadow: 0 0 0 1px rgba(0, 0, 255, 0.5), inset 0 0 0 1px rgba(0, 0, 255, 0.5);\n  outline: 1px solid rgba(0, 0, 255, 0.5);\n}\n")), document.head.appendChild(P)
                }
            }, Z = {
                init(t) {
                    t || (t = document.body), function (t) {
                        const e = document.createElement("DIV");
                        e.innerHTML = '<div class="wah-controls">\n  <div class="wah-control">\n    <label for="wah-control-hotarea">走查热区</label>\n    <input type="checkbox" id="wah-control-hotarea" />\n  </div>\n  <div class="wah-control">\n    <label for="wah-control-reading">走查读屏</label>\n    <input type="checkbox" id="wah-control-reading" />\n  </div>\n</div>', e.addEventListener("input", (e => {
                            const n = e.target, r = n.checked;
                            switch (n.id) {
                                case"wah-control-hotarea":
                                    r ? H.show() : H.hide();
                                    break;
                                case"wah-control-reading":
                                    M.hide(), r && M.show(t)
                            }
                        }), !1), t.appendChild(e)
                    }(t)
                }
            }
        })(), r
    })()
}));