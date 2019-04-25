function uploadAction() {
     var formData = new FormData(document.getElementById("upload_form"));

     $.ajax({
        type: 'POST',
        url: "/api/file-service/upload",
        data: formData,
        processData:false,
        contentType:false,
        success: function (data) {
            console.log(data);
            var error = document.getElementById("error");
            error.textContent="上传成功";
            error.hidden=false;
        },
        error: function(data) {
            console.log(data);
            var error = document.getElementById("error");
            error.textContent="上传失败";
            error.hidden=false;
        },
        async: true
    });
}

function deleteAction() {
     var fileID = document.getElementById("fileID").value;
     $.ajax({
        type: 'DELETE',
        url: "/api/file-service/delete?" + $.param({ id: fileID }),
        data: {},
        success: function (data) {
            console.log(data);
            var error = document.getElementById("error");
            error.textContent="删除成功";
            error.hidden=false;
        },
        error: function(data) {
            console.log(data);
            var error = document.getElementById("error");
            error.textContent="删除失败";
            error.hidden=false;
        },
        async: true
    });
}