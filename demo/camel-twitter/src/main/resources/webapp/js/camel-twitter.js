$(document).ready(function () {

    var socket;

    $('#connect_form').submit(function () {

        var host = $("#url").val();

        socket = new WebSocket(host);

        $('#connect').fadeOut({ duration:'fast' });
        $('#disconnect').fadeIn();
        $('#send_form_input').removeAttr('disabled');

        // Add a connect listener
        socket.onopen = function () {
            $('#msg').append('<p class="event">Socket Twitter Status: ' + socket.readyState + ' (open)</p>');
        }

        socket.onmessage = function (msg) {

            // $('#msg').append('<p class="message">Received: ' + msg.data + "</p>");

            var message = JSON.parse(msg.data);

            // extract the tweets
            var txt = message.tweet;

            var $res = $("<li/>");
            $res.append(txt);
            $res.appendTo($('#tweet'));

            setInterval(function () {
                tickTweet()
            }, 5000);
            $('#tweetList').show();

        }

        socket.onclose = function () {
            $('#msg').append('<p class="event">Socket Twitter Status: ' + socket.readyState + ' (Closed)</p>');
        }


        return false;
    });

    $('#disconnect_form').submit(function () {

        socket.close();

        $('#msg').append('<p class="event">Socket Twitter Status: ' + socket.readyState + ' (Closed)</p>');

        $('#disconnect').fadeOut({ duration:'fast' });
        $('#connect').fadeIn();
        $('#send_form_input').addAttr('disabled');

        return false;
    });

});

function tickTweet() {
    $('#tweet li:first').slideUp(function () {
        $(this).appendTo($('#tweet')).slideDown();
    });
}

$(function() {
    $( "#tabs" ).tabs();
});