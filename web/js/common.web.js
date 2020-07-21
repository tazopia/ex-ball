var path = '/';
try {
    path = location.pathname.match(/^(\/.*?)\//)[1];
} catch (e) {
}

window.onbeforeunload = function (event) {
    if (event.clientY < 0 || event.altKey || event.ctrlKey || (event.clientY < 129 && event.clientY > 109)) {
        $.ajax({
            url: '/logout'
        });
    }
};

$(window).on('scroll load', function(){
    if ($(window).scrollTop() > $(window).height()) {
        $('#btn-top').addClass('show');
    } else {
        $('#btn-top').removeClass('show');
    }
});

$('#btn-top').on('click', function(e) {
    e.preventDefault();
    e.stopPropagation();

    $('html, body').stop().animate({
        scrollTop: 0
    }, 300);
});

(function ($) {
    var rolling = function () {
        $('#rolling').animate({
            left: -$('#rolling').width()
        }, 30000, 'linear', function () {
            $('#rolling').css({left: $('#rolling-wrap').width()});
            rolling();
        });
    };
    setTimeout(function () {
        rolling();
    }, 1000);
})(jQuery);

var showMessage = function (message) {
    alert(message);
};

var errorMessage = function (message) {
    alert(message);
};

var _date = new Date(parseInt($('#milliseconds').text(), 10));
var clock = new FlipClock($('#clock'), {
    clockFace: 'TwentyFourHourClock'
});
clock.setTime(_date);

var goPage = function (page) {
    $('#page').val(page);
    $('#frm-search').submit();
};

var goSearch = function () {
    $('#page').val(1);
    $('#frm-search').submit();
};

var point = {
    exchange: function () {
        if (!confirm('포인트를 머니로 전환하겠습니까?')) {
            return false;
        }

        $.post(path + '/payment/exchange').done(function (data) {
            if (data.success) {
                var money = parseInt(data.value);
                $('#user-money').text((parseInt($('#user-money').text().num()) + money).toString().money());
                $('#user-point').text(0);
            }
        });
    }
};

var payment = {
    account: function () {
        if (!confirm('계좌문의를 하시겠습니까?')) {
            return false;
        }
        $.post(path + '/customer/account').done(function (data) {
            if (data.success) {
                location.href = path + '/customer/qna';
            }
        });
    }
};

$('#lotto').on('click', function () {
    $(this).attr('src', '/images/lotto.gif');
    $.ajax({
        url: path + '/event/lotto',
        success: function (data) {
            setTimeout(function () {
                alert(data.message);
                if (data.success) {
                    var point = parseInt(data.value);
                    $('#user-point').text((parseInt($('#user-point').text().num(), 10) + point).toString().money());
                }
                $('#lotto').attr('src', '/images/lotto.png');
            }, 2500)
        }
    });
});