function bohuang(){var W='',T=' top: -1000px;',rb='" for "gwt:onLoadErrorFn"',pb='" for "gwt:onPropertyErrorFn"',ab='");',sb='#',Eb='&',Zb='.cache.js',ub='/',Ab='//',Tb='1D3A9169F2041A166865CE72EDEBC9C6',Ub='4A1E3673278B3FBA646CC2F32FDDA3CF',Wb='4DFD1B3FFF998CDC8B42D26938F1059D',Yb=':',jb='::',V='<!doctype html>',X='<html><head><\/head><body><\/body><\/html>',mb='=',tb='?',ob='Bad handler "',U='CSS1Compat',$='Chrome',Z='DOMContentLoaded',O='DUMMY',Xb='F8E127427689426CD1C0A8B31C38BEF7',Hb='Unexpected exception in locale detection, using default: ',Gb='_',Fb='__gwt_Locale',Jb='android',zb='base',xb='baseUrl',J='begin',Nb='blackberry',P='body',M='bohuang',Sb='bohuang.devmode.js',yb='bohuang.nocache.js',ib='bohuang::',I='bootstrap',wb='clear.cache.gif',lb='content',Cb='default',ec='end',_='eval("',Ob='file://',K='gwt.codesvr.bohuang=',L='gwt.codesvr=',dc='gwt/clean/clean.css',qb='gwt:onLoadErrorFn',nb='gwt:onPropertyErrorFn',kb='gwt:property',fb='head',bc='href',Q='iframe',vb='img',Kb='ipad',Mb='iphone',Lb='ipod',cb='javascript',R='javascript:""',$b='link',cc='loadExternalRefs',Bb='locale',Db='locale=',gb='meta',eb='moduleRequested',db='moduleStartup',hb='name',Qb='no',Ib='phonegap.env',S='position:absolute; width:0; height:0; border:none; left: -1000px;',_b='rel',bb='script',Rb='selectingPermutation',N='startup',ac='stylesheet',Y='undefined',Pb='yes',Vb='zh';var q=window;var r=document;t(I,J);function s(){var a=q.location.search;return a.indexOf(K)!=-1||a.indexOf(L)!=-1}
function t(a,b){if(q.__gwtStatsEvent){q.__gwtStatsEvent({moduleName:M,sessionId:q.__gwtStatsSessionId,subSystem:N,evtGroup:a,millis:(new Date).getTime(),type:b})}}
bohuang.__sendStats=t;bohuang.__moduleName=M;bohuang.__errFn=null;bohuang.__moduleBase=O;bohuang.__softPermutationId=0;bohuang.__computePropValue=null;bohuang.__getPropMap=null;bohuang.__gwtInstallCode=function(){};bohuang.__gwtStartLoadingFragment=function(){return null};var u=function(){return false};var v=function(){return null};__propertyErrorFunction=null;var w=q.__gwt_activeModules=q.__gwt_activeModules||{};w[M]={moduleName:M};var x;function y(){A();return x}
function z(){A();return x.getElementsByTagName(P)[0]}
function A(){if(x){return}var a=r.createElement(Q);a.src=R;a.id=M;a.style.cssText=S+T;a.tabIndex=-1;r.body.appendChild(a);x=a.contentDocument;if(!x){x=a.contentWindow.document}x.open();var b=document.compatMode==U?V:W;x.write(b+X);x.close()}
function B(k){function l(a){function b(){if(typeof r.readyState==Y){return typeof r.body!=Y&&r.body!=null}return /loaded|complete/.test(r.readyState)}
var c=b();if(c){a();return}function d(){if(!c){c=true;a();if(r.removeEventListener){r.removeEventListener(Z,d,false)}if(e){clearInterval(e)}}}
if(r.addEventListener){r.addEventListener(Z,d,false)}var e=setInterval(function(){if(b()){d()}},50)}
function m(c){function d(a,b){a.removeChild(b)}
var e=z();var f=y();var g;if(navigator.userAgent.indexOf($)>-1&&window.JSON){var h=f.createDocumentFragment();h.appendChild(f.createTextNode(_));for(var i=0;i<c.length;i++){var j=window.JSON.stringify(c[i]);h.appendChild(f.createTextNode(j.substring(1,j.length-1)))}h.appendChild(f.createTextNode(ab));g=f.createElement(bb);g.language=cb;g.appendChild(h);e.appendChild(g);d(e,g)}else{for(var i=0;i<c.length;i++){g=f.createElement(bb);g.language=cb;g.text=c[i];e.appendChild(g);d(e,g)}}}
bohuang.onScriptDownloaded=function(a){l(function(){m(a)})};t(db,eb);var n=r.createElement(bb);n.src=k;r.getElementsByTagName(fb)[0].appendChild(n)}
bohuang.__startLoadingFragment=function(a){return E(a)};bohuang.__installRunAsyncCode=function(a){var b=z();var c=y().createElement(bb);c.language=cb;c.text=a;b.appendChild(c);b.removeChild(c)};function C(){var c={};var d;var e;var f=r.getElementsByTagName(gb);for(var g=0,h=f.length;g<h;++g){var i=f[g],j=i.getAttribute(hb),k;if(j){j=j.replace(ib,W);if(j.indexOf(jb)>=0){continue}if(j==kb){k=i.getAttribute(lb);if(k){var l,m=k.indexOf(mb);if(m>=0){j=k.substring(0,m);l=k.substring(m+1)}else{j=k;l=W}c[j]=l}}else if(j==nb){k=i.getAttribute(lb);if(k){try{d=eval(k)}catch(a){alert(ob+k+pb)}}}else if(j==qb){k=i.getAttribute(lb);if(k){try{e=eval(k)}catch(a){alert(ob+k+rb)}}}}}v=function(a){var b=c[a];return b==null?null:b};__propertyErrorFunction=d;bohuang.__errFn=e}
function D(){function e(a){var b=a.lastIndexOf(sb);if(b==-1){b=a.length}var c=a.indexOf(tb);if(c==-1){c=a.length}var d=a.lastIndexOf(ub,Math.min(c,b));return d>=0?a.substring(0,d+1):W}
function f(a){if(a.match(/^\w+:\/\//)){}else{var b=r.createElement(vb);b.src=a+wb;a=e(b.src)}return a}
function g(){var a=v(xb);if(a!=null){return a}return W}
function h(){var a=r.getElementsByTagName(bb);for(var b=0;b<a.length;++b){if(a[b].src.indexOf(yb)!=-1){return e(a[b].src)}}return W}
function i(){var a=r.getElementsByTagName(zb);if(a.length>0){return a[a.length-1].href}return W}
function j(){var a=r.location;return a.href==a.protocol+Ab+a.host+a.pathname+a.search+a.hash}
var k=g();if(k==W){k=h()}if(k==W){k=i()}if(k==W&&j()){k=e(r.location.href)}k=f(k);return k}
function E(a){if(a.match(/^\//)){return a}if(a.match(/^[a-zA-Z]+:\/\//)){return a}return bohuang.__moduleBase+a}
function F(){var i=[];var j;function k(a,b){var c=i;for(var d=0,e=a.length-1;d<e;++d){c=c[a[d]]||(c[a[d]]=[])}c[a[e]]=b}
var l=[];var m=[];function n(a){var b=m[a](),c=l[a];if(b in c){return b}var d=[];for(var e in c){d[c[e]]=e}if(__propertyErrorFunc){__propertyErrorFunc(a,d,b)}throw null}
m[Bb]=function(){var b=null;var c=Cb;try{if(!b){var d=location.search;var e=d.indexOf(Db);if(e>=0){var f=d.substring(e+7);var g=d.indexOf(Eb,e);if(g<0){g=d.length}b=d.substring(e+7,g)}}if(!b){b=v(Bb)}if(!b){b=q[Fb]}if(b){c=b}while(b&&!u(Bb,b)){var h=b.lastIndexOf(Gb);if(h<0){b=null;break}b=b.substring(0,h)}}catch(a){alert(Hb+a)}q[Fb]=c;return b||Cb};l[Bb]={'default':0,zh:1};m[Ib]=function(){{var a=window.navigator.userAgent.toLowerCase();if(a.indexOf(Jb)!=-1||(a.indexOf(Kb)!=-1||(a.indexOf(Lb)!=-1||(a.indexOf(Mb)!=-1||a.indexOf(Nb)!=-1)))){var b=document.location.href;if(b.indexOf(Ob)===0){return Pb}}return Qb}};l[Ib]={no:0,yes:1};u=function(a,b){return b in l[a]};bohuang.__getPropMap=function(){var a={};for(var b in l){if(l.hasOwnProperty(b)){a[b]=n(b)}}return a};bohuang.__computePropValue=n;q.__gwt_activeModules[M].bindings=bohuang.__getPropMap;t(I,Rb);if(s()){return E(Sb)}var o;try{k([Cb,Qb],Tb);k([Cb,Pb],Ub);k([Vb,Pb],Wb);k([Vb,Qb],Xb);o=i[n(Bb)][n(Ib)];var p=o.indexOf(Yb);if(p!=-1){j=parseInt(o.substring(p+1),10);o=o.substring(0,p)}}catch(a){}bohuang.__softPermutationId=j;return E(o+Zb)}
function G(){if(!q.__gwt_stylesLoaded){q.__gwt_stylesLoaded={}}function c(a){if(!__gwt_stylesLoaded[a]){var b=r.createElement($b);b.setAttribute(_b,ac);b.setAttribute(bc,E(a));r.getElementsByTagName(fb)[0].appendChild(b);__gwt_stylesLoaded[a]=true}}
t(cc,J);c(dc);t(cc,ec)}
C();bohuang.__moduleBase=D();w[M].moduleBase=bohuang.__moduleBase;var H=F();G();t(I,ec);B(H);return true}
bohuang.succeeded=bohuang();