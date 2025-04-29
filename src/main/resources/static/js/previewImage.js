document.getElementById("files").addEventListener("change", function(event) {
    var imagePreviewContainer = document.getElementById("imagePreview");
    var files = event.target.files;

    // 선택한 이미지들을 미리보기로 추가
    for (var i = 0; i < files.length; i++) {
        var file = files[i];
        var reader = new FileReader();

        reader.onload = function(e) {
            var img = document.createElement("img");
            img.src = e.target.result;
            img.classList.add("img-thumbnail");
            img.style.margin = "5px";
            img.style.width = "20%";
            img.style.height = "100%";
            img.style.objectFit = "contain";

            // 미리보기 컨테이너에 이미지를 추가
            imagePreviewContainer.appendChild(img);
        };

        // 이미지를 읽어서 미리보기로 추가
        reader.readAsDataURL(file);
    }
});


