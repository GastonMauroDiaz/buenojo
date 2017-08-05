angular.module('buenOjoApp')
  .directive('scaleMeter', function() {


    function link(scope,element,attrs){
        scope.canvas = element[0];

        scope.$watch('properties',function(newValue){
          scope.paint(newValue);
        },true);

        scope.paint = function(properties) {
          if (!properties) return;
          var canvas        = scope.canvas;
          var width         = canvas.width;
          var height        = canvas.height;
          var context       = canvas.getContext('2d');
          var scaleLineY    = height - (height * 0.25);
          var segmentUnits  = Math.floor(properties.scaleUnits/properties.segments);
          var segmentPixels = Math.floor(segmentUnits/properties.resolution);
          var scalePixels   = segmentPixels * properties.segments;
          context.clearRect(0,0,width,height);
          context.beginPath();
          //base line
          context.strokeStyle = properties.color;
          context.moveTo(0, scaleLineY);
          context.lineTo(scalePixels, scaleLineY);

          // segments
          var i=0;

          for(i=0;i<=properties.segments;i++){
            var segmentLineX = i*segmentPixels;

            context.moveTo(segmentLineX,scaleLineY);
            context.lineTo(segmentLineX,scaleLineY-properties.scaleHeight);
            context.font = properties.textFont;
            context.fillText((i*segmentUnits)+properties.unit,segmentLineX+(i==0?0:properties.textPadding) , scaleLineY-properties.scaleHeight);
          }
          context.lineWidth = properties.lineWidth ;
          context.stroke();

        };


      var defaults = {
        resolution:   13.53,
        scaleHeight:  7,
        segments:     5,
        scaleUnits:   5000,
        unit:         'm',
        lineWidth:    0.5,
        textPadding:  -10,
        textFont:     '5pt Arial',
        color:        'black'

      };
      if (!scope.properties)
        scope.properties = defaults;
      else {
        var newProperties = {}
        newProperties.scaleHeight	= (scope.properties && scope.properties.scaleHeight)?scope.properties.scaleHeight:defaults.scaleHeight;
        newProperties.resolution   = (scope.properties && scope.properties.resolution)?scope.properties.resolution:defaults.resolution;
        newProperties.scaleUnits   = (scope.properties && scope.properties.scaleUnits)?scope.properties.scaleUnits:defaults.scaleUnits;
        newProperties.segments     = (scope.properties && scope.properties.segments)?scope.properties.segments:defaults.segments;
        newProperties.textFont     = (scope.properties && scope.properties.textFont)?scope.properties.textFont:defaults.textFont;
        newProperties.unit			    = (scope.properties && scope.properties.unit)?scope.properties.unit:defaults.unit;
        newProperties.color      	= (scope.properties && scope.properties.color)?scope.properties.color:defaults.color;
        newProperties.textPadding	= (scope.properties && scope.properties.textPadding)?scope.properties.textPadding:defaults.textPadding;
        newProperties.lineWidth    = (scope.properties && scope.properties.lineWidth)?scope.properties.lineWidth:defaults.lineWidth;
        //var elementId     = (scope.properties.elementId)?scope.properties.elementId:defaults.elementId;
        scope.properties = newProperties;
      }



    }
    return {
      scope: {
        properties: '='

      },
      replace: true,
      transclude: true,
      restrict: 'E',

      template: '<canvas></canvas>',
      link: link

    };
  });
