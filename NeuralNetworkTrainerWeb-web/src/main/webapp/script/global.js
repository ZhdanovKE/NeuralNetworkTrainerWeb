
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

class NetworkRemover {
    
    constructor(nameInputId, deleteBtnId) {
        this.nameInputId = nameInputId;
        this.deleteBtnId = deleteBtnId;
        
    }
    
    confirmDelete(name) {
        if (confirm("Do you really want to delete network '" + name + "'?")) {
            let nameInput = document.getElementById(this.nameInputId);
            let deleteBtn = document.getElementById(this.deleteBtnId);
            nameInput.value = name;
            deleteBtn.click();
        }
    }
}

class NetworkDownloader {
    
    constructor(nameInputId, saveAsTextCheckId, downloadBtnId) {
        this.nameInputId = nameInputId;
        this.saveAsTextCheckId = saveAsTextCheckId;
        this.downloadBtnId = downloadBtnId;
    }
    
    downloadAsBinary(name) {
        let nameInput = document.getElementById(this.nameInputId);
        let saveAsTextCheck = document.getElementById(this.saveAsTextCheckId);
        let downloadBtn = document.getElementById(this.downloadBtnId);
        nameInput.value = name;
        saveAsTextCheck.checked = false;
        downloadBtn.click();
    }
    
    downloadAsText(name) {
        let nameInput = document.getElementById(this.nameInputId);
        let saveAsTextCheck = document.getElementById(this.saveAsTextCheckId);
        let downloadBtn = document.getElementById(this.downloadBtnId);
        nameInput.value = name;
        saveAsTextCheck.checked = true;
        downloadBtn.click();
    }
}
