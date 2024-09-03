var timerhandle = null;
var __fpageurl = "";

function getPageWidth() 
{
//alert(2);

	var LinkedIds=AndroidFunction.GetLinkedIds();
	ConvertLinkedIdsToIndex(LinkedIds);
	
	AndroidFunction.SetPageWidth(document.body.scrollWidth);
	
    __fpageurl = AndroidFunction.GetPageUrl();
	
	window.setTimeout(function (){
                if (__fpageurl != "") 
                {
                	clearTimer();
                    window.location.href = __fpageurl;
                }

            }, 42);
}
 
 
 
window.addEventListener('load', getPageWidth);

//alert(1);

timerhandle= window.setTimeout(getPageWidth, 10000);

function clearTimer()
{
	if (timerhandle!=null)
    	window.clearTimeout(timerhandle);
}

function ConvertLinkedIdsToIndex(LinkedIds)
{
//alert("LinkedIds" + LinkedIds);
   			var ids = LinkedIds;
 			 var idArray = ids.split(',');
 			
            var ColWidth = parseInt(document.getElementById("f_p_container").style.margin)*2 + parseInt(document.getElementById("f_p_container").style.width);

            var AryIndex = [];
            var AryIds = [];
            for (var i = 0; i < idArray.length; i++) {
                if (document.getElementById(idArray[i].trim()) == null || document.getElementById(idArray[i].trim()) == undefined) continue;

                var val = document.getElementById(idArray[i].trim()).offsetLeft; //+ document.getElementById(idArray[i].trim()).offsetWidth
               // colNo = parseInt((val - 30) / ColWidth)
                colNo = parseInt((val + parseInt(document.getElementById("f_p_container").style.margin)) / ColWidth);

                if (colNo < 0)
                    colNo = 0;
                

                AryIndex.push(colNo);
                AryIds.push(idArray[i]);

            }

            // console.log(AryIndex.join(',') + "#" + AryIds.join(','))
           // alert("join " + AryIndex.join(',') + "#" + AryIds.join(','));
			AndroidFunction.SetPageLinkedIds(AryIndex.join(',') + "#" + AryIds.join(','));
}

        