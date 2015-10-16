module.exports = function(ctx) {
    var Q = ctx.requireCordovaModule('q');
    var deferral = new Q.defer();
    
    var exec = require('child_process').exec, child;

    var cmdExecute = 'unzip -f plugins/com.keepe.cardio/src/ios/CardIO/libCardIO.a.zip -d plugins/com.keepe.cardio/src/ios/CardIO/'
    child = exec(cmdExecute, function(error, stdout, stderr) {
        if (error !== null) {
            console.log('exec error: ' + error);
            deferral.reject('Operation failed');
        } else {
            console.log('exec unzip complete done: ' + stdout);
            deferral.resolve();
        }
    });

    return deferral.promise;
};