var path="/";try{path=location.pathname.match(/^(\/.*?)\//)[1]}catch(e){}$(function(){var e=$("#flashMessage");""!==e.text().trim()&&setTimeout(function(){showMessage(e.text())},10);var a=$(".table-game input, .sel");a.on("focus",function(){$(this).select()});var s=$(".cal");s.calendar();var t=$("#sp-reset-date");t.on("click",function(){s.val("")})});var showMessage=function(e){$("#flashMessage div").html(e),$("#flashMessage").removeClass("error").stop(!0,!0).slideDown().delay(1e3).slideUp()},errorMessage=function(e){$("#flashMessage div").html(e),$("#flashMessage").addClass("error").stop(!0,!0).slideDown().delay(1e3).slideUp()},goPage=function(e){$("#page").val(e),$("#frm-search").submit()},goSearch=function(){$("#page").val(1),$("#frm-search").submit()};