$(document).ready(function() {
    /*$.ajax({
        type: "GET",
        url: "/kalah-1.0/getAvatars",
        async: false,
        contentType: "application/json",
        success: function (res) {
            console.log(res);
            if (res.result == "success") {
                var avatars = res.avatars;
                for (var i = 0; i < avatars.length; i++) {
                    var avatar = avatars[i];
                    var li = $("<li id='li-"+avatar.id+"'><img src='"+avatar.path+"' class='left circle'><span>"+avatar.name+"</span></li>");
                    $("ul").append(li);
                    var option = $("<option id='av-"+avatar.id+"' data-icon='"+avatar.path+"' class='left circle'>"+avatar.name+"</option>");
                    $("#icons").append(option);
                }
            } else {

            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error status code: " + xhr.status);
            alert("Error details: " + thrownError);
        }
    });*/

    $("#signup").on("click", function () {
        var data = {};
        data.login = $("#name").val();
        data.password = $("#password").val();
        data.email = $("#email").val();
        data.avatar = $("#icons").val();
        console.log(data);
        $.ajax({
            type: "POST",
            url: "/kalah-1.0/registration",
            async: true,
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (res) {
                console.log(res);
                if (res.result == "success") {
                    location.href = '/kalah-1.0/profile.html';
                } else {
                    alert("Пользователь с таким именем уже существует.");
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Error status code: " + xhr.status);
                alert("Error details: " + thrownError);
            }
        });
    });
});