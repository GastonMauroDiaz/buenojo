'use strict';

angular.module('buenOjoApp')
    .service('BuenOjoMathUtils', function() {
        this.scale = function(actualSize, naturalSize) {
            if (naturalSize === 0) throw "NaturalSize can't be zero";
            return actualSize / naturalSize;
        };
        this.roundNumber = function(num, dec) {
            return Math.round(num * Math.pow(10, dec)) / Math.pow(10, dec);
        };

        this.roundInt = function(number, figures) {
            if (figures >= this.getNumberFigures(number)) throw 'figures are bigger than the number to be rounded';
            return this.roundNumber(number, -figures);

        };
        this.getNumberFigures = function(number) {
            var n = Math.floor(number / 10);
            var f = 1;
            while (n > 0) {
                f++;
                n = Math.floor(n / 10);
            }
            return f;
        };

        this.distance = function(x1, y1, x2, y2) {
            return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
        };

        this.shuffle = function(array) {
            var counter = array.length;

            // While there are elements in the array
            while (counter > 0) {
                // Pick a random index
                var index = Math.floor(Math.random() * counter);

                // Decrease counter by 1
                counter--;

                // And swap the last element with it
                var temp = array[counter];
                array[counter] = array[index];
                array[index] = temp;
            }

            return array;
        };
        this.randomIntFromInterval = function (min,max) {
          return Math.floor(Math.random()*(max-min+1)+min);
        };


    });
