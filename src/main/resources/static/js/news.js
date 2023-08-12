let newsDiv = $("#news")
let postForm = $("#postForm")
let postFormTitle = $("#postFormTitle")
let postFormButton = $("#postFormButton")
let titleInput = $("#title")
let textInput = $("#text")

$(".btn-create").on("click", createPost)
$(".btn-edit").on("click", editPost)
$(".btn-delete").on("click", deletePost)

function createPost () {
    titleInput.val("")
    textInput.val("")

    postFormTitle.text("Создание поста")
    postFormButton.text("Создать").on("click", function () {
        if (titleInput.val() !== "" && textInput.val() !== "") {
            $.ajax({
                url: "/news",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    title: titleInput.val(),
                    text:  textInput.val()
                }),
                success: function (result) {
                    if (result === "blank title")
                        alert("Заголовок поста не может быть пустым")
                    else if (result === "blank text")
                        alert("Текст поста не может быть пустым")
                    else {
                        $(".no-news").remove()
                        newsDiv.html(`
                        <div class="card border-info mb-3" id="${result}">
                            <div class="card-body">
                                <h5 class="card-title" id="${result}-title" style="font-size: ${postTitleSize}">
                                    ${titleInput.val()}
                                </h5>
                                <hr class="w-50 mx-auto">
                                <p class="card-text" id="${result}-text" style="font-size: ${postTitleSize}">
                                    ${textInput.val()}
                                </p>
                                <div><p class="small text-secondary">Только что</p></div>
                                <button class="text-info btn btn-outline-info btn-edit"">
                                    Изменить
                                </button>
                                <button class="text-danger btn btn-outline-danger btn-delete"">❌</button>
                            </div>
                        </div>
                        ` + newsDiv.html())
                        postForm.modal("hide")
                        $(".btn-edit").on("click", editPost)
                        $(".btn-delete").on("click", deletePost)
                    }
                }
            })
            postFormButton.off("click")
        } else alert("Заголовок и текст поста не могут быть пустыми")
    })
    postForm.modal("show")
}

function editPost () {
    let postId = $(this).parents(".card")[0].id
    console.log(postId)
    titleInput.val($("#" + postId + "-title").text())
    textInput.val($("#" + postId + "-text").text())

    postFormTitle.text("Изменение поста")
    postFormButton.text("Сохранить").on("click", function () {
        let title = titleInput.val()
        let text = textInput.val()
        $.ajax({
            url: "/news",
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify({
                id: postId,
                title: title,
                text: text
            }),
            success: function (result) {
                if (result === "blank title")
                    alert("Заголовок поста не может быть пустым")
                else if (result === "blank text")
                    alert("Текст поста не может быть пустым")
                else if (result === "success") {
                    $("#" + postId + "-title").text(title);
                    $("#" + postId + "-text").text(text);
                    postForm.modal("hide")
                } else alert("Post with id " + postId + " was not updated")
            }
        })
        postFormButton.off("click")
    })
    postForm.modal("show")
}

function deletePost () {
    let postId = $(this).parents(".card")[0].id
    $.ajax({
        url: "/news",
        type: "DELETE",
        contentType: "application/json",
        data: JSON.stringify({
            id: postId
        }),
        success: function(result) {
            if (result === "success")
                $("#" + postId).remove();
            else if (result === "id not found")
                alert("Post with id " + postId + " not found")

            if (newsDiv.children().length === 0)
                newsDiv.html(`
                    <p class="h5 text-info text-center no-news">Новостей нет</p>
                `)
        },
    });
}