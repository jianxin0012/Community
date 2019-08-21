function post() {
    var questionId = $("#question_id").val();
    var content = $("#content").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1
        }),
        success: function (response) {
            debugger;
            if (response.code==200){
                $("#comment-section").hide();
            }else {
                if (response.code == 2002) {
                    var conf = confirm(response.message);
                    if (conf) {
                        window.open("https://github.com/login/oauth/authorize?client_id=Iv1.09e20c5f5aa79a60&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closable","true");
                    }
                }else {
                    alert(response.message);
                }

            }
        },
        dataType: "json"
    });
}