let newsDiv = $("#news")
let postForm = $("#postForm")
let postFormPopup = $("#postFormPopup")
let postFormTitle = $("#postFormTitle")
let postFormButton = $("#postFormButton")
let titleInput = $("#title")
let textInput = $("#text")
let imageInput = $("#image")
let uploadedImagesDiv = $("#uploaded-images")
let uploadedImagesNames = []

$(".btn-create").on("click", createPost)
$(".btn-edit").on("click", editPost)
$(".btn-delete").on("click", deletePost)

function generateImagesBlock(postId, files, spinners = false, carouselInterval = false, carouselArrows = true) {
    if (files.length === 0)
        return ``
    else if (files.length === 1)
        return spinners ?
            `<div class="spinner-border text-info mt-3 mx-auto d-block" id="image-spinner-0" role="status"></div>` :
            `<img src="${URL.createObjectURL(files[0])}" alt="Image"
                        class="img-fluid img-thumbnail mx-auto d-block rounded-3 uploaded-image mt-3">`
    else {
        let indicators = `
                <li data-bs-target="#post-${postId}-carousel" data-bs-slide-to="0" class="active"></li>`
        let items = spinners ?
            `<div class="carousel-item active" id="post-${postId}-image-0">
                    <div class="spinner-border text-info my-5 mx-auto d-block" id="image-spinner-0" role="status"></div>
                </div>` :
            `<div class="carousel-item active" id="post-${postId}-image-0">
                    <img src="${URL.createObjectURL(files[0])}" alt="Image 1"
                        class="img-fluid img-thumbnail mx-auto d-block rounded-3 uploaded-image mt-3">
                </div>`
        let arrows = carouselArrows ?
            `<a class="carousel-control-prev" href="#post-${postId}-carousel" role="button" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Previous</span>
            </a>
            <a class="carousel-control-next" href="#post-${postId}-carousel" role="button" data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Next</span>
            </a>` : ``
        for (let imageIndex = 1; imageIndex < files.length; imageIndex++) {
            indicators += `<li data-bs-target="#post-${postId}-carousel" data-bs-slide-to="${imageIndex}"></li>`
            items += spinners ?
                `<div class="carousel-item" id="post-${postId}-image-${imageIndex}">
                    <div class="spinner-border text-info my-5 mx-auto d-block" id="image-spinner-${imageIndex}" role="status"></div>
                </div>` :
                `<div class="carousel-item" id="post-${postId}-image-${imageIndex}">
                    <img src="${URL.createObjectURL(files[imageIndex])}" alt="Image ${imageIndex + 1}"
                        class="img-fluid img-thumbnail mx-auto d-block rounded-3 uploaded-image mt-3">
                </div>`
        }
        return `
            <div id="post-${postId}-carousel" class="carousel slide mt-3" data-ride="carousel" data-bs-keyboard="true" data-bs-interval="${carouselInterval}">
              <ol class="carousel-indicators" id="post-${postId}-indicators">
                ${indicators}
              </ol>
              <div class="carousel-inner" id="post-${postId}-items">
                ${items}
              </div>
              ${arrows}
            </div>`
    }
}

imageInput.on("change", function () {
    $("#uploaded-images").html("")
    uploadedImagesNames = []
    let files = imageInput.prop("files")
    uploadedImagesDiv.html(generateImagesBlock("uploaded", files, true))
    $("#post-uploaded-carousel").css("width", "100%")
    if (files.length === 1) {
        let file = files[0]
        let data = new FormData()
        data.append("name", file.name)
        data.append("file", file)
        $.ajax({
            url: "/images-upload",
            type: "POST",
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            success: function (result) {
                if (result !== 'fail') {
                    let imageId = result
                    uploadedImagesNames.push(imageId)
                    $(`#image-spinner-0`).remove()
                    uploadedImagesDiv.html(`
                        <img src="${URL.createObjectURL(file)}" alt="Image ${imageId}"
                        class="img-fluid img-thumbnail mx-auto d-block rounded-3 uploaded-image mt-3">
                    ` + uploadedImagesDiv.html());
                    $("#post-uploaded-carousel").css("width", "fit-content")
                } else {
                    console.log(result)
                }
            }
        })
    } else {
        for (let imageIndex = 0; imageIndex < files.length; imageIndex++) {
            let file = files[imageIndex]
            let data = new FormData()
            data.append("name", file.name)
            data.append("file", file)
            $.ajax({
                url: "/images-upload",
                type: "POST",
                data: data,
                cache: false,
                contentType: false,
                processData: false,
                success: function (result) {
                    if (result !== 'fail') {
                        let imageId = result
                        uploadedImagesNames.push(imageId)
                        $(`#post-uploaded-image-${imageIndex}`).html(
                            `<img src="${URL.createObjectURL(file)}" alt="Image ${imageId}" 
                                    class="img-fluid d-block rounded mx-auto uploaded-image">`);
                        $("#post-uploaded-carousel").css("width", "fit-content")
                    } else {
                        console.log(result)
                    }
                }
            })
        }
    }
})


function createPost() {
    titleInput.val("")
    textInput.val("")
    imageInput.val("")
    uploadedImagesDiv.html("")
    uploadedImagesNames = []

    postFormTitle.text("Создание поста")
    postFormButton.text("Создать")
    postForm.on("submit", function (e) {
        e.preventDefault();
        $.ajax({
            url: "/news",
            type: "POST",
            data: JSON.stringify({
                "title": titleInput.val(),
                "text": textInput.val(),
                "images": uploadedImagesNames
            }),
            contentType: "application/json",
            success: function (result) {
                if (![
                    "Заголовок поста не может быть пустым",
                    "Текст поста не может быть пустым",
                    "Пост не создан"
                ].includes(result)) {
                    let postId = result
                    $(".no-news").remove()
                    newsDiv.html(`
                    <div class="card border-info mb-3" id="post-${postId}">
                        <div class="card-body">
                            <h5 class="card-title" id="post-${postId}-title" style="font-size: ${postTitleSize}">${titleInput.val()}</h5>
                            <hr class="w-50 mx-auto">
                            <p class="card-text" id="post-${postId}-text" style="font-size: ${postTextSize}">${textInput.val()}</p>
                            <div id="post-${postId}-images">
                                ${generateImagesBlock(postId, imageInput.prop("files"), false, 3000)}
                            </div>
                            <div><p class="small text-secondary">Только что</p></div>
                            <button class="text-info btn btn-outline-info btn-edit"">
                                Изменить
                            </button>
                            <button class="text-danger btn btn-outline-danger btn-delete"">❌</button>
                        </div>
                    </div>
                    ` + newsDiv.html())
                    $(".btn-edit").on("click", editPost)
                    $(".btn-delete").on("click", deletePost)
                } else alert(result)
                postFormPopup.modal("hide")
            }
        })
        postForm.off("submit")
    })
    postFormPopup.modal("show")
}

function editPost () {
    let postId = $(this).parents(".card")[0].id.substring(5)
    titleInput.val($(`#post-${postId}-title`).text())
    textInput.val($(`#post-${postId}-text`).text())
    uploadedImagesDiv.html($(`#post-${postId}-images`).html())
    uploadedImagesNames = []
    let files = null;
    imageInput.on("change", function () {
        files = imageInput.prop("files")
    })

    postFormTitle.text("Изменение поста")
    postFormButton.text("Сохранить")
    postForm.on("submit", function (e) {
        e.preventDefault();
        let title = titleInput.val()
        let text = textInput.val()

        $.ajax({
            url: "/news",
            type: "PUT",
            data: JSON.stringify({
                "id": postId,
                "title": titleInput.val(),
                "text": textInput.val(),
                "images": uploadedImagesNames
            }),
            contentType: "application/json",
            success: function (result) {
                console.log("EDIT: " + result)
                if (![
                    "Заголовок поста не может быть пустым",
                    "Текст поста не может быть пустым",
                    "Пост не найден"
                ].includes(result)) {
                    $(`#post-${postId}-title`).text(title);
                    $(`#post-${postId}-text`).text(text);
                    if (files)
                        $(`#post-${postId}-images`)
                            .html("")
                            .html(generateImagesBlock(postId, files, false, 3000))
                } else alert(result)
                postFormPopup.modal("hide")
            }
        })
        postForm.off("submit")
    })
    postFormPopup.modal("show")
}

function deletePost () {
    let postId = $(this).parents(".card")[0].id.substring(5)
    $.ajax({
        url: "/news",
        type: "DELETE",
        contentType: "application/json",
        data: JSON.stringify({
            id: postId
        }),
        success: function(result) {
            if (!["Пост не найден"].includes(result)) {
                console.log(postId)
                $(`#post-${postId}`).remove();
            } else alert(result)

            if (newsDiv.children().length === 0)
                newsDiv.html(`<p class="h5 text-info text-center no-news">Новостей нет</p>`)
        },
    });
}
