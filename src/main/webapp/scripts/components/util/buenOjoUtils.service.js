'use strict';

angular.module('buenOjoApp')
    .service('BuenOjoUtils', function(ActivityType) {
        this.numberFromID = function(elementID) {
            var r = /\d+/;
            var n = r.exec(elementID);
            return parseInt(n, 10);
        };
        this.secondsToMinutes = function(seconds){
           var m = moment.duration(seconds,'seconds');
           return m.format("mm:ss",{trim: false});//m.get('minutes')+':'+m.get('seconds');
        }
        this.duration = function(fromDate, toDate) {
            if (!fromDate || !toDate) return "";

            var momentTo = moment(toDate,"YYYY/MM/DD HH:mm:ss.SSSZ");
            var momentFrom = moment(fromDate,"YYYY/MM/DD HH:mm:ss.SSSZ")
            var diff = momentTo.diff(momentFrom);

            return moment.utc(diff).format('mm:ss');


          };

        this.findIndexInArray = function (array, callback) {

            if (!Array.prototype.findIndex) {
                Array.prototype.findIndex = function(predicate) {
                    'use strict';
                    if (this == null) {
                        throw new TypeError('Array.prototype.findIndex called on null or undefined');
                    }
                    if (typeof predicate !== 'function') {
                        throw new TypeError('predicate must be a function');
                    }
                    var list = Object(this);
                    var length = list.length >>> 0;
                    var thisArg = arguments[1];
                    var value;

                    for (var i = 0; i < length; i++) {
                        value = list[i];
                        if (predicate.call(thisArg, value, i, list)) {
                            return i;
                        }
                    }
                    return -1;
                };
            }

            return array.findIndex(callback)
        };

        this.pushEsriTopoToBack = function(array){
            var index = this.findIndexInArray(array, function(element,index,array){
              return element.imageType === "EsriTopo";
            });
            var lastIndex = array.length-1;
            if (index >=0 && index < lastIndex) {
              this.swapItems(array,index,lastIndex);
            }
        };

        this.swapItems = function(array, a,b){
          var aux= array[a];
          array[a] = array[b];
          array[b] = aux;

        };

        this.describeActivity = function(activity) {
           return ActivityType.describe(activity.type);
        }

    });
