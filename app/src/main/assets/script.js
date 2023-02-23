
'use strict';

    function test() {
        var l = document.getElementsByTagName('svg')[0];
        var e = document.createEvent('HTMLEvents');
        e.initEvent('click',true,true);
        l.dispatchEvent(e);

        var x = document.getElementsByClassName("plyr__control plyr__control--overlaid")[0];
        var y = document.createEvent('HTMLEvents');
        y.initEvent('click',true,true);
        z.dispatchEvent(e);
    }
