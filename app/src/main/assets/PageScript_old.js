function  DisplayImageInPopup(ImagePath) 
  {
	 event.preventDefault();
 	 event.stopPropagation();
 	 AndroidFunction.DisplayImageInPopup(ImagePath);
  } 
window.onclick=function(e)   
  {
  	// AndroidFunction.ShowActionBar();
  	  //AndroidFunction.ShowPageNavigationBar();
  	  
  	   if (e.target != null && e.target.tagName != null) 
    	{
    		var node=null;
    		if( e.target.tagName.toUpperCase() == "A")
    			node=e.target;
    		else if(e.target.parentNode!=null &&  e.target.parentNode.tagName.toUpperCase() == "A")
    			node=e.target.parentNode;
    		else if(e.target.parentNode.parentNode!=null &&  e.target.parentNode.parentNode.tagName.toUpperCase() == "A")
    			node=e.target.parentNode.parentNode;
    		
    		if (node==null)
    		{
    		AndroidFunction.ShowActionBar();
        	AndroidFunction.ShowPageNavigationBar();
         	AndroidFunction.HideHighlight();
    		} 
    		
     		if (node!=null && node.href != null && node.href != undefined && node.href != "") 
        		{
        		  event.preventDefault();
            	  event.stopPropagation();
            	  GotoLink(node.href);
        		}
    	}
    
  };

  
  
 /* function GotoLink(Url)
  {
alert(Url);
	var BookType=AndroidFunction.GetBookType();
	//alert(BookType);
	 if (BookType.toUpperCase() =="VERTICALSCROLL") {
	 
	    if (window.location.href.indexOf("#") == -1) 
        {
           // alert("inside if");
            //alert("window:" + window.location.href);
           // alert("URl:" +Url );
            //window.location.href = window.location.href + Url;
            location.href = Url;
        }
        else 
        {
            //alert("inside else");
            //window.location.href = window.location.href.split("#")[0] + Url;
            location.href = Url;
        }
    }
    else if (Url.indexOf("http") == 0) {
        AndroidFunction.DisplayUrlInDefaultBrowser(Url);
    }
    else {
       // alert("pageNAvigation");
        AndroidFunction.PageNavigate(Url);
    }
   	 
  }*/
  
  function GotoLink(Url)
  {

   	  if (Url.indexOf("#") == 0) {
   	 	 return;
       /* if (window.location.href.indexOf("#") == -1) {
           alert("inside if");
           
            window.location.href = window.location.href + Url;
        }
        else {
           
             alert("inside else");
            window.location.href = window.location.href.split("#")[0] + Url;
        }*/
        
    }
    else if (Url.indexOf("http") == 0) {
        AndroidFunction.DisplayUrlInDefaultBrowser(Url);
    }
    else {
   // alert("pageNAvigation");
        //AndroidFunction.DisplayErrorMessage(Url + " PageNavigate");
        AndroidFunction.PageNavigate(Url);
    }
  }
  
  
  
 
  
var OrhLeft = -99999;
function SetFontSize(fontsize)
  {
    document.getElementsByTagName("body")[0].style.fontSize = fontsize;


    var oStyle = window.getComputedStyle(document.getElementById("f_p_child_div_c"))

    

  	var SW = document.getElementById("f_p_child_div_c").scrollWidth
  	var l=0;
  	if (OrhLeft == -99999)
  	{
  //	alert("oStyle left " + oStyle["left"]);
  	    l = parseFloat(oStyle["left"]);
  	    if (l < 0)
  	        l = -1 * l;

  	    OrhLeft = l;
  	}
  	else
  	{
  	    l = OrhLeft;
  	}


  	var m = parseFloat(oStyle["margin"]);
  	m = m * 2;

  	var w = parseFloat(oStyle["width"]);
  	
  	var CW = l + m + w;
  	
  	var ColNumber = 0;
  	//alert("SW" + SW +",width=" +w+ ",margin="+m +",l="+l);
  	
  	if (SW <CW)
  	{
  	    ColNumber = parseInt(SW / (w + m - 1));

  	    var l1 = -1*ColNumber* (m + w)
  	    
  	    document.getElementById("f_p_child_div_c").style.left = l1 + "px";
  	}
  	else
  	{
  	    ColNumber = l/w;     //parseInt((SW / (CW-1)));


  	    document.getElementById("f_p_child_div_c").style.left = (-1 * OrhLeft) + "px";
  	}
  	//if(parseInt(ColNumber)==0)
  //	{
  //		ColNumber=1;
  //	}
  	AndroidFunction.setColumnNumber(parseInt(ColNumber));
  	//return ColNumber;
  }  
  
  
  var OrhLeft = -99999;
function SetBackgroundColor(backgroundcolor)
  {
   var res = backgroundcolor.split(",");
   
    document.getElementsByTagName("body")[0].style.backgroundColor = res[0];
    
	document.getElementsByTagName("body")[0].style.color=res[1];


    var oStyle = window.getComputedStyle(document.getElementById("f_p_child_div_c"))

    

  	var SW = document.getElementById("f_p_child_div_c").scrollWidth
  	var l=0;
  	if (OrhLeft == -99999)
  	{
  //	alert("oStyle left " + oStyle["left"]);
  	    l = parseFloat(oStyle["left"]);
  	    if (l < 0)
  	        l = -1 * l;

  	    OrhLeft = l;
  	}
  	else
  	{
  	    l = OrhLeft;
  	}


  	var m = parseFloat(oStyle["margin"]);
  	m = m * 2;

  	var w = parseFloat(oStyle["width"]);
  	
  	var CW = l + m + w;
  	
  	var ColNumber = 0;
  	//alert("SW" + SW +",width=" +w+ ",margin="+m +",l="+l);
  	
  	if (SW <CW)
  	{
  	    ColNumber = parseInt(SW / (w + m - 1));

  	    var l1 = -1*ColNumber* (m + w)
  	    
  	    document.getElementById("f_p_child_div_c").style.left = l1 + "px";
  	}
  	else
  	{
  	    ColNumber = l/w;     //parseInt((SW / (CW-1)));


  	    document.getElementById("f_p_child_div_c").style.left = (-1 * OrhLeft) + "px";
  	}
  	//if(parseInt(ColNumber)==0)
  //	{
  //		ColNumber=1;
  //	}
  	AndroidFunction.setColumnNumber(parseInt(ColNumber));
  	//return ColNumber;
  }  
  
  