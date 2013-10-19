function haoxiangzuo(){
  var $intern_14 = '', $intern_11 = ' top: -1000px;', $intern_37 = '" for "gwt:onLoadErrorFn"', $intern_35 = '" for "gwt:onPropertyErrorFn"', $intern_20 = '");', $intern_38 = '#', $intern_67 = '.cache.js', $intern_40 = '/', $intern_46 = '//', $intern_60 = '15356E16092722E1B65DAC59A61DDD75', $intern_61 = '69D8826904659C2E102809E00483DF83', $intern_62 = '7864BEEBA5A9EF32758140A893C19E65', $intern_63 = '8197AF1B83C73A333E65B898AD0988EC', $intern_64 = '98819BF6AF3B32F5B226F603D2D6FC41', $intern_66 = ':', $intern_29 = '::', $intern_13 = '<!doctype html>', $intern_15 = '<html><head><\/head><body><\/body><\/html>', $intern_32 = '=', $intern_39 = '?', $intern_34 = 'Bad handler "', $intern_12 = 'CSS1Compat', $intern_18 = 'Chrome', $intern_17 = 'DOMContentLoaded', $intern_6 = 'DUMMY', $intern_65 = 'EB74338DD592F45878A7549C87F13EB4', $intern_45 = 'base', $intern_43 = 'baseUrl', $intern_1 = 'begin', $intern_7 = 'body', $intern_0 = 'bootstrap', $intern_42 = 'clear.cache.gif', $intern_31 = 'content', $intern_74 = 'end', $intern_19 = 'eval("', $intern_55 = 'gecko', $intern_56 = 'gecko1_8', $intern_2 = 'gwt.codesvr.haoxiangzuo=', $intern_3 = 'gwt.codesvr=', $intern_73 = 'gwt/clean/clean.css', $intern_36 = 'gwt:onLoadErrorFn', $intern_33 = 'gwt:onPropertyErrorFn', $intern_30 = 'gwt:property', $intern_4 = 'haoxiangzuo', $intern_59 = 'haoxiangzuo.devmode.js', $intern_44 = 'haoxiangzuo.nocache.js', $intern_28 = 'haoxiangzuo::', $intern_25 = 'head', $intern_71 = 'href', $intern_54 = 'ie6', $intern_53 = 'ie8', $intern_52 = 'ie9', $intern_8 = 'iframe', $intern_41 = 'img', $intern_22 = 'javascript', $intern_9 = 'javascript:""', $intern_68 = 'link', $intern_72 = 'loadExternalRefs', $intern_26 = 'meta', $intern_24 = 'moduleRequested', $intern_23 = 'moduleStartup', $intern_51 = 'msie', $intern_27 = 'name', $intern_48 = 'opera', $intern_10 = 'position:absolute; width:0; height:0; border:none; left: -1000px;', $intern_69 = 'rel', $intern_50 = 'safari', $intern_21 = 'script', $intern_58 = 'selectingPermutation', $intern_5 = 'startup', $intern_70 = 'stylesheet', $intern_16 = 'undefined', $intern_57 = 'unknown', $intern_47 = 'user.agent', $intern_49 = 'webkit';
  var $wnd = window;
  var $doc = document;
  sendStats($intern_0, $intern_1);
  function isHostedMode(){
    var query = $wnd.location.search;
    return query.indexOf($intern_2) != -1 || query.indexOf($intern_3) != -1;
  }

  function sendStats(evtGroupString, typeString){
    if ($wnd.__gwtStatsEvent) {
      $wnd.__gwtStatsEvent({moduleName:$intern_4, sessionId:$wnd.__gwtStatsSessionId, subSystem:$intern_5, evtGroup:evtGroupString, millis:(new Date).getTime(), type:typeString});
    }
  }

  haoxiangzuo.__sendStats = sendStats;
  haoxiangzuo.__moduleName = $intern_4;
  haoxiangzuo.__errFn = null;
  haoxiangzuo.__moduleBase = $intern_6;
  haoxiangzuo.__softPermutationId = 0;
  haoxiangzuo.__computePropValue = null;
  haoxiangzuo.__getPropMap = null;
  haoxiangzuo.__gwtInstallCode = function(){
  }
  ;
  haoxiangzuo.__gwtStartLoadingFragment = function(){
    return null;
  }
  ;
  var __gwt_isKnownPropertyValue = function(){
    return false;
  }
  ;
  var __gwt_getMetaProperty = function(){
    return null;
  }
  ;
  __propertyErrorFunction = null;
  var activeModules = $wnd.__gwt_activeModules = $wnd.__gwt_activeModules || {};
  activeModules[$intern_4] = {moduleName:$intern_4};
  var frameDoc;
  function getInstallLocationDoc(){
    setupInstallLocation();
    return frameDoc;
  }

  function getInstallLocation(){
    setupInstallLocation();
    return frameDoc.getElementsByTagName($intern_7)[0];
  }

  function setupInstallLocation(){
    if (frameDoc) {
      return;
    }
    var scriptFrame = $doc.createElement($intern_8);
    scriptFrame.src = $intern_9;
    scriptFrame.id = $intern_4;
    scriptFrame.style.cssText = $intern_10 + $intern_11;
    scriptFrame.tabIndex = -1;
    $doc.body.appendChild(scriptFrame);
    frameDoc = scriptFrame.contentDocument;
    if (!frameDoc) {
      frameDoc = scriptFrame.contentWindow.document;
    }
    frameDoc.open();
    var doctype = document.compatMode == $intern_12?$intern_13:$intern_14;
    frameDoc.write(doctype + $intern_15);
    frameDoc.close();
  }

  function installScript(filename){
    function setupWaitForBodyLoad(callback){
      function isBodyLoaded(){
        if (typeof $doc.readyState == $intern_16) {
          return typeof $doc.body != $intern_16 && $doc.body != null;
        }
        return /loaded|complete/.test($doc.readyState);
      }

      var bodyDone = isBodyLoaded();
      if (bodyDone) {
        callback();
        return;
      }
      function onBodyDone(){
        if (!bodyDone) {
          bodyDone = true;
          callback();
          if ($doc.removeEventListener) {
            $doc.removeEventListener($intern_17, onBodyDone, false);
          }
          if (onBodyDoneTimerId) {
            clearInterval(onBodyDoneTimerId);
          }
        }
      }

      if ($doc.addEventListener) {
        $doc.addEventListener($intern_17, onBodyDone, false);
      }
      var onBodyDoneTimerId = setInterval(function(){
        if (isBodyLoaded()) {
          onBodyDone();
        }
      }
      , 50);
    }

    function installCode(code){
      function removeScript(body, element){
      }

      var docbody = getInstallLocation();
      var doc = getInstallLocationDoc();
      var script;
      if (navigator.userAgent.indexOf($intern_18) > -1 && window.JSON) {
        var scriptFrag = doc.createDocumentFragment();
        scriptFrag.appendChild(doc.createTextNode($intern_19));
        for (var i = 0; i < code.length; i++) {
          var c = window.JSON.stringify(code[i]);
          scriptFrag.appendChild(doc.createTextNode(c.substring(1, c.length - 1)));
        }
        scriptFrag.appendChild(doc.createTextNode($intern_20));
        script = doc.createElement($intern_21);
        script.language = $intern_22;
        script.appendChild(scriptFrag);
        docbody.appendChild(script);
        removeScript(docbody, script);
      }
       else {
        for (var i = 0; i < code.length; i++) {
          script = doc.createElement($intern_21);
          script.language = $intern_22;
          script.text = code[i];
          docbody.appendChild(script);
          removeScript(docbody, script);
        }
      }
    }

    haoxiangzuo.onScriptDownloaded = function(code){
      setupWaitForBodyLoad(function(){
        installCode(code);
      }
      );
    }
    ;
    sendStats($intern_23, $intern_24);
    var script = $doc.createElement($intern_21);
    script.src = filename;
    $doc.getElementsByTagName($intern_25)[0].appendChild(script);
  }

  haoxiangzuo.__startLoadingFragment = function(fragmentFile){
    return computeUrlForResource(fragmentFile);
  }
  ;
  haoxiangzuo.__installRunAsyncCode = function(code){
    var docbody = getInstallLocation();
    var script = getInstallLocationDoc().createElement($intern_21);
    script.language = $intern_22;
    script.text = code;
    docbody.appendChild(script);
  }
  ;
  function processMetas(){
    var metaProps = {};
    var propertyErrorFunc;
    var onLoadErrorFunc;
    var metas = $doc.getElementsByTagName($intern_26);
    for (var i = 0, n = metas.length; i < n; ++i) {
      var meta = metas[i], name = meta.getAttribute($intern_27), content;
      if (name) {
        name = name.replace($intern_28, $intern_14);
        if (name.indexOf($intern_29) >= 0) {
          continue;
        }
        if (name == $intern_30) {
          content = meta.getAttribute($intern_31);
          if (content) {
            var value, eq = content.indexOf($intern_32);
            if (eq >= 0) {
              name = content.substring(0, eq);
              value = content.substring(eq + 1);
            }
             else {
              name = content;
              value = $intern_14;
            }
            metaProps[name] = value;
          }
        }
         else if (name == $intern_33) {
          content = meta.getAttribute($intern_31);
          if (content) {
            try {
              propertyErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_34 + content + $intern_35);
            }
          }
        }
         else if (name == $intern_36) {
          content = meta.getAttribute($intern_31);
          if (content) {
            try {
              onLoadErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_34 + content + $intern_37);
            }
          }
        }
      }
    }
    __gwt_getMetaProperty = function(name){
      var value = metaProps[name];
      return value == null?null:value;
    }
    ;
    __propertyErrorFunction = propertyErrorFunc;
    haoxiangzuo.__errFn = onLoadErrorFunc;
  }

  function computeScriptBase(){
    function getDirectoryOfFile(path){
      var hashIndex = path.lastIndexOf($intern_38);
      if (hashIndex == -1) {
        hashIndex = path.length;
      }
      var queryIndex = path.indexOf($intern_39);
      if (queryIndex == -1) {
        queryIndex = path.length;
      }
      var slashIndex = path.lastIndexOf($intern_40, Math.min(queryIndex, hashIndex));
      return slashIndex >= 0?path.substring(0, slashIndex + 1):$intern_14;
    }

    function ensureAbsoluteUrl(url){
      if (url.match(/^\w+:\/\//)) {
      }
       else {
        var img = $doc.createElement($intern_41);
        img.src = url + $intern_42;
        url = getDirectoryOfFile(img.src);
      }
      return url;
    }

    function tryMetaTag(){
      var metaVal = __gwt_getMetaProperty($intern_43);
      if (metaVal != null) {
        return metaVal;
      }
      return $intern_14;
    }

    function tryNocacheJsTag(){
      var scriptTags = $doc.getElementsByTagName($intern_21);
      for (var i = 0; i < scriptTags.length; ++i) {
        if (scriptTags[i].src.indexOf($intern_44) != -1) {
          return getDirectoryOfFile(scriptTags[i].src);
        }
      }
      return $intern_14;
    }

    function tryBaseTag(){
      var baseElements = $doc.getElementsByTagName($intern_45);
      if (baseElements.length > 0) {
        return baseElements[baseElements.length - 1].href;
      }
      return $intern_14;
    }

    function isLocationOk(){
      var loc = $doc.location;
      return loc.href == loc.protocol + $intern_46 + loc.host + loc.pathname + loc.search + loc.hash;
    }

    var tempBase = tryMetaTag();
    if (tempBase == $intern_14) {
      tempBase = tryNocacheJsTag();
    }
    if (tempBase == $intern_14) {
      tempBase = tryBaseTag();
    }
    if (tempBase == $intern_14 && isLocationOk()) {
      tempBase = getDirectoryOfFile($doc.location.href);
    }
    tempBase = ensureAbsoluteUrl(tempBase);
    return tempBase;
  }

  function computeUrlForResource(resource){
    if (resource.match(/^\//)) {
      return resource;
    }
    if (resource.match(/^[a-zA-Z]+:\/\//)) {
      return resource;
    }
    return haoxiangzuo.__moduleBase + resource;
  }

  function getCompiledCodeFilename(){
    var answers = [];
    var softPermutationId;
    function unflattenKeylistIntoAnswers(propValArray, value){
      var answer = answers;
      for (var i = 0, n = propValArray.length - 1; i < n; ++i) {
        answer = answer[propValArray[i]] || (answer[propValArray[i]] = []);
      }
      answer[propValArray[n]] = value;
    }

    var values = [];
    var providers = [];
    function computePropValue(propName){
      var value = providers[propName](), allowedValuesMap = values[propName];
      if (value in allowedValuesMap) {
        return value;
      }
      var allowedValuesList = [];
      for (var k in allowedValuesMap) {
        allowedValuesList[allowedValuesMap[k]] = k;
      }
      if (__propertyErrorFunc) {
        __propertyErrorFunc(propName, allowedValuesList, value);
      }
      throw null;
    }

    providers[$intern_47] = function(){
      var ua = navigator.userAgent.toLowerCase();
      var makeVersion = function(result){
        return parseInt(result[1]) * 1000 + parseInt(result[2]);
      }
      ;
      if (function(){
        return ua.indexOf($intern_48) != -1;
      }
      ())
        return $intern_48;
      if (function(){
        return ua.indexOf($intern_49) != -1;
      }
      ())
        return $intern_50;
      if (function(){
        return ua.indexOf($intern_51) != -1 && $doc.documentMode >= 9;
      }
      ())
        return $intern_52;
      if (function(){
        return ua.indexOf($intern_51) != -1 && $doc.documentMode >= 8;
      }
      ())
        return $intern_53;
      if (function(){
        var result = /msie ([0-9]+)\.([0-9]+)/.exec(ua);
        if (result && result.length == 3)
          return makeVersion(result) >= 6000;
      }
      ())
        return $intern_54;
      if (function(){
        return ua.indexOf($intern_55) != -1;
      }
      ())
        return $intern_56;
      return $intern_57;
    }
    ;
    values[$intern_47] = {gecko1_8:0, ie6:1, ie8:2, ie9:3, opera:4, safari:5};
    __gwt_isKnownPropertyValue = function(propName, propValue){
      return propValue in values[propName];
    }
    ;
    haoxiangzuo.__getPropMap = function(){
      var result = {};
      for (var key in values) {
        if (values.hasOwnProperty(key)) {
          result[key] = computePropValue(key);
        }
      }
      return result;
    }
    ;
    haoxiangzuo.__computePropValue = computePropValue;
    $wnd.__gwt_activeModules[$intern_4].bindings = haoxiangzuo.__getPropMap;
    sendStats($intern_0, $intern_58);
    if (isHostedMode()) {
      return computeUrlForResource($intern_59);
    }
    var strongName;
    try {
      unflattenKeylistIntoAnswers([$intern_50], $intern_60);
      unflattenKeylistIntoAnswers([$intern_48], $intern_61);
      unflattenKeylistIntoAnswers([$intern_52], $intern_62);
      unflattenKeylistIntoAnswers([$intern_53], $intern_63);
      unflattenKeylistIntoAnswers([$intern_54], $intern_64);
      unflattenKeylistIntoAnswers([$intern_56], $intern_65);
      strongName = answers[computePropValue($intern_47)];
      var idx = strongName.indexOf($intern_66);
      if (idx != -1) {
        softPermutationId = parseInt(strongName.substring(idx + 1), 10);
        strongName = strongName.substring(0, idx);
      }
    }
     catch (e) {
    }
    haoxiangzuo.__softPermutationId = softPermutationId;
    return computeUrlForResource(strongName + $intern_67);
  }

  function loadExternalStylesheets(){
    if (!$wnd.__gwt_stylesLoaded) {
      $wnd.__gwt_stylesLoaded = {};
    }
    function installOneStylesheet(stylesheetUrl){
      if (!__gwt_stylesLoaded[stylesheetUrl]) {
        var l = $doc.createElement($intern_68);
        l.setAttribute($intern_69, $intern_70);
        l.setAttribute($intern_71, computeUrlForResource(stylesheetUrl));
        $doc.getElementsByTagName($intern_25)[0].appendChild(l);
        __gwt_stylesLoaded[stylesheetUrl] = true;
      }
    }

    sendStats($intern_72, $intern_1);
    installOneStylesheet($intern_73);
    sendStats($intern_72, $intern_74);
  }

  processMetas();
  haoxiangzuo.__moduleBase = computeScriptBase();
  activeModules[$intern_4].moduleBase = haoxiangzuo.__moduleBase;
  var filename = getCompiledCodeFilename();
  loadExternalStylesheets();
  sendStats($intern_0, $intern_74);
  installScript(filename);
  return true;
}

haoxiangzuo.succeeded = haoxiangzuo();
