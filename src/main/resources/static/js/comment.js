/**
 * 提交回复
 */

function post() {
    var questionId = $("#question_id").val();
    var content = $("#content").val();

    if (!content) {
        alert("回复内容不能为空");
        return;
    }

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
            if (response.code==200){
                window.location.reload();
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

/**
 * 展开二级评论
 */
function comment_collapse(e) {
    var id = e.getAttribute("data");
    var secondComment = $("#comment-"+id);
    if (!secondComment.hasClass("in")){
        //展开评论
        $.getJSON("/comment/"+id,function (data) {
            var sec_comment = $("#comment-"+id);
            if (sec_comment.children().length != 1) {
                secondComment.addClass("in");
                e.classList.add("active");
            }else {
                $.each(data.data,function (index,comment) {
                    var c = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                        html:comment.content
                    });
                    sec_comment.prepend(c);
                });
                secondComment.addClass("in");
                e.classList.add("active");
            }
        });
    }else {
        //折叠评论
        secondComment.removeClass("in");
        e.classList.remove("active");
    }

}

function comment(e) {
    var id = e.getAttribute("data");
    var content = $("#input-"+id).val();

    if (!content) {
        alert("回复内容不能为空");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parentId":id,
            "content":content,
            "type":2
        }),
        success: function (response) {
            if (response.code==200){
                window.location.reload();
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

function comment_like(e) {
    var id = e.getAttribute("data");
    console.log(id);
    $.getJSON("/comment_like/"+id,function (response) {
        console.log(response);
        if (response.code == 200) {
            // var oldLikeCount = $("#comment-likeCount-"+id).val();
            // $("#comment-likeCount-"+id).val(oldLikeCount+1);
            window.location.reload();
        }else {
            alert(response.message);
        }
    })
}