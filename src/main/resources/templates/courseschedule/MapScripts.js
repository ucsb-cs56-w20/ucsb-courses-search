

   function getWalkTime(){
       var travelFrom = document.getElementById("travel-from");
       var travelFromCoords = travelFrom.options[travelFrom.selectedIndex].value;

       var travelTo = document.getElementById("travel-to");
       var travelToCoords = travelTo.options[travelTo.selectedIndex].value;

       window.open("https://maps.openrouteservice.org/directions?n1=34.412592&n2=-119.845887&n3=16&a="
+ travelFromCoords + "," + travelToCoords + "&b=2&c=0&k1=en-US&k2=km");
   }

   function getBikeTime(){
       var travelFrom = document.getElementById("travel-from");
       var travelFromCoords = travelFrom.options[travelFrom.selectedIndex].value;

       var travelTo = document.getElementById("travel-to");
       var travelToCoords = travelTo.options[travelTo.selectedIndex].value;

       window.open("https://maps.openrouteservice.org/directions?n1=34.412592&n2=-119.845887&n3=16&a="
+ travelFromCoords + "," + travelToCoords + "&b=1&c=0&k1=en-US&k2=km")
   }
