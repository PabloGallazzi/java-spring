app.service('errorService', function(){

    return {
        showApiError : function (error) {
            if(error){
                return error.replace(new RegExp("_", 'g')," ");
            }
        }
    }

});