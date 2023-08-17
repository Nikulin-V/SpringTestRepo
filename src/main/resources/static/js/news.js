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
                <li data-bs-target="#uploadedImagesCarousel" data-bs-slide-to="0" class="active"></li>`
        let items = spinners ?
            `<div class="carousel-item active" id="image-0">
                    <div class="spinner-border text-info my-5 mx-auto d-block" id="image-spinner-0" role="status"></div>
                </div>` :
            `<div class="carousel-item active" id="image-0">
                    <img src="${URL.createObjectURL(files[0])}" alt="Image 1"
                        class="img-fluid img-thumbnail mx-auto d-block rounded-3 uploaded-image mt-3">
                </div>`
        let arrows = carouselArrows ?
            `<a class="carousel-control-prev" href="#${postId}-carousel" role="button" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Previous</span>
            </a>
            <a class="carousel-control-next" href="#${postId}-carousel" role="button" data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Next</span>
            </a>` : ``
        for (let imageIndex = 1; imageIndex < files.length; imageIndex++) {
            indicators += `<li data-bs-target="#uploadedImagesCarousel" data-bs-slide-to="${imageIndex}"></li>`
            items += spinners ?
                `<div class="carousel-item" id="image-${imageIndex}">
                    <div class="spinner-border text-info my-5 mx-auto d-block" id="image-spinner-${imageIndex}" role="status"></div>
                </div>` :
                `<div class="carousel-item" id="image-${imageIndex}">
                    <img src="${URL.createObjectURL(files[imageIndex])}" alt="Image ${imageIndex + 1}"
                        class="img-fluid img-thumbnail mx-auto d-block rounded-3 uploaded-image mt-3">
                </div>`
        }
        return `
            <div id="${postId}-carousel" class="carousel slide mt-3" data-ride="carousel" data-bs-keyboard="true" data-bs-interval="${carouselInterval}">
              <ol class="carousel-indicators" id="${postId}-indicators">
                ${indicators}
              </ol>
              <div class="carousel-inner" id="${postId}-items">
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
    uploadedImagesDiv.html(generateImagesBlock("uploadedImagesCarousel", files, true))
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
                        $(`#image-${imageIndex}`).html(
                            `<img src="${URL.createObjectURL(file)}" alt="Image ${imageId}" 
                                    class="img-fluid d-block rounded mx-auto uploaded-image">`);
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
        if (titleInput.val() !== "" && textInput.val() !== "") {
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
                    console.log("CREATE: " + result)
                    if (result === "blank title") alert("Заголовок поста не может быть пустым")
                    else if (result === "blank text") alert("Текст поста не может быть пустым")
                    else if (result === "fail") alert("Пост не создан")
                    else {
                        let postId = result
                        $(".no-news").remove()
                        newsDiv.html(`
                        <div class="card border-info mb-3" id="${postId}">
                            <div class="card-body">
                                <h5 class="card-title" id="${postId}-title" style="font-size: ${postTitleSize}">${titleInput.val()}</h5>
                                <hr class="w-50 mx-auto">
                                <p class="card-text" id="${postId}-text" style="font-size: ${postTitleSize}">${textInput.val()}</p>
                                <div id="${postId}-images">
                                    ${generateImagesBlock(`carousel-${postId}`, imageInput.prop("files"), false, 3000, false)}
                                </div>
                                <div><p class="small text-secondary">Только что</p></div>
                                <button class="text-info btn btn-outline-info btn-edit"">
                                    Изменить
                                </button>
                                <button class="text-danger btn btn-outline-danger btn-delete"">❌</button>
                            </div>
                        </div>
                        ` + newsDiv.html())
                        postFormPopup.modal("hide")
                        $(".btn-edit").on("click", editPost)
                        $(".btn-delete").on("click", deletePost)
                    }
                }
            })
            postForm.off("submit")
        } else alert("Заголовок и текст поста не могут быть пустыми")
    })
    postFormPopup.modal("show")
}

function editPost () {
    let postId = $(this).parents(".card")[0].id
    titleInput.val($(`#${postId}-title`).text())
    textInput.val($(`#${postId}-text`).text())
    uploadedImagesDiv.html($(`#${postId}-images`).html())
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
                if (result === "blank title") alert("Заголовок поста не может быть пустым")
                else if (result === "blank text") alert("Текст поста не может быть пустым")
                else if (result === "fail") alert("Пост с id " + postId + " не был изменён")
                else {
                    $(`#${postId}-title`).text(title);
                    $(`#${postId}-text`).text(text);
                    if (files)
                        $(`#${postId}-images`).html("")
                            .html(
                                generateImagesBlock(`carousel-${postId}`, files, false, 3000, false)
                            )
                    postFormPopup.modal("hide")
                }
            }
        })
        postForm.off("submit")
    })
    postFormPopup.modal("show")
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
            if (result === "fail") alert("Пост с id " + postId + " не был удалён")
            else if (result === "id not found") alert("Пост с id " + postId + " не найден")
            else $("#" + postId).remove();

            if (newsDiv.children().length === 0)
                newsDiv.html(`<p class="h5 text-info text-center no-news">Новостей нет</p>`)
        },
    });
}
