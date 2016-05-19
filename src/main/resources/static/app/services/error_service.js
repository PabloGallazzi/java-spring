app.service('errorService', function(){

    return {
        showApiError : function (error) {
            return error.replace(new RegExp("_", 'g')," ");
        }
    }

});