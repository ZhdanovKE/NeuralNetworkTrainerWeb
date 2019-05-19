
class FileUploader {
    constructor(fileInputId, validationErrorMsgId, uploadBtnId, maxFileSize) {
        this.fileInputId = fileInputId;
        this.validationErrorMsgId = validationErrorMsgId;
        this.uploadBtnId = uploadBtnId;
        this.maxFileSize = maxFileSize;
    }
    
    fileTooLarge(file) {
        return file.size > this.maxFileSize;
    }
    
    handleUpload() {
        let fileInput = document.getElementById(this.fileInputId);
        if (!fileInput) {
            return;
        }
        let validationErrorMsg = document.getElementById(this.validationErrorMsgId);  
        if (!validationErrorMsg) {
            return;
        }
        validationErrorMsg.value = "";
        for (let index = 0; index < fileInput.files.length; index++) {
            if (this.fileTooLarge(fileInput.files[index])) {
                validationErrorMsg.value = "File is too large (max size is " +
                        this.maxFileSize
                        + " bytes): " + 
                        fileInput.files[index].name;
                fileInput.value = "";
                break;
            }
        }
        document.getElementById(this.uploadBtnId).click();
    }
}


