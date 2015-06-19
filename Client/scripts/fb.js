// This is called with the results from from FB.getLoginStatus().

	fid = "";
	moder = false;
	sfid = "";
	alist = "";
	
	getingPostTime = null;
	serverReqTime = null;
	fbLogginCheckTime = null;
	fbLogginCheckTimeStart = null;
  function statusChangeCallback(response) {
	  fbLogginCheckTime = new Date().getTime() - fbLogginCheckTimeStart;
	  console.log(fbLogginCheckTimeStart);
	  console.log(fbLogginCheckTime);
    
    // The response object is returned with a status field that lets the
    // app know the current login status of the person.
    // Full docs on the response object can be found in the documentation
    // for FB.getLoginStatus().
	
    if (response.status == 'connected') {
		$('#loginModal').modal('hide');
		FB.api('/me', function(response) {
			fid = response.id;
			url = window.location.href;
			if(url.indexOf("index.html") == url.length-"index.html".length)
				$.ajax({url: "view/hello.html"}).done(function (html) {
				$("#mainCnt").html(html);
			
			start = new Date().getTime();
			$.ajax({url: "http://artistrecommender-priest.rhcloud.com/rest/artists"})
			.done(function (data) {
				serverReqTime = new Date().getTime() - start;
				$.ajax({url: "http://artistrecommender-priest.rhcloud.com/rest/subartists/"+response.id+""})
				.done(function (subbed) {
				alist = "";
				
					
				
				for(i = 0; i<data.length;i++){
					if(subbed != undefined && subbed.indexOf(data[i].facebookId) >= 0) alist += "<li><a href=\"index.html?id="+data[i].facebookId+"\"><i class=\"fa fa-caret-right\"></i> "+data[i].name+"</a></li>";
					
					astr = "<div class=\"panel panel-default\"> \
									 <div class=\"panel-heading\" style=\"height:60px;\"><h4 style=\"float: left;\">"+data[i].name+"</h4>";
									 if(subbed == undefined || subbed.indexOf(data[i].facebookId) == -1) astr += "<button type=\"button\" onclick=\"subbToArtist("+data[i].facebookId+")\" class=\"btn btn-success pull-right\">Subscribe</button>";
									 if(subbed != undefined && subbed.indexOf(data[i].facebookId) >= 0) astr += "<button type=\"button\" onclick=\"unsubbToArtist("+data[i].facebookId+")\" class=\"btn btn-danger pull-right\">Unsubscribe</button>";
									 astr += "</div> \
									  <div class=\"panel-body\" style=\"clear: left;\"> \
										<div><img align=\"left\" style=\"margin-right: 10px; margin-bottom: 10px;\" height=\"180px\" width=\"180px\" src=\"http://graph.facebook.com/"+data[i].facebookId+"/picture?width=720&height=720\"> \
										"+data[i].description+" \
										</div> \
										<div class=\"clearfix\"></div> \
									  </div> \
								   </div>";
								   
					$("#allCnt").before(astr);
				}
				$("#lg-menu").after(alist);
				$("#serverReqTimeId").html("Server artists respone time: " +serverReqTime + " milliseconds");
			
			});
			});
			
			$("#allCnt").before("<div class=\"panel panel-default\"><div class=\"panel-heading\" style=\"text-align:center; vertical-align: text-top;\"><h3>Hello, "+response.name+"</h3></div></div>");
			$("#fbLogginCheckTimeId").html("Checking facebook login takes: " +fbLogginCheckTime + " milliseconds");
			});
			
			else
			{
				if(url.indexOf("id=") > -1)
				{
					alist = "";
					$.ajax({url: "http://artistrecommender-priest.rhcloud.com/rest/moderators/"+fid})
					.done(function (mod) {
						if(mod == fid)
							moder = true;
					start = new Date().getTime();
					$.ajax({url: "http://artistrecommender-priest.rhcloud.com/rest/artists"})
					.done(function (data) {
						serverReqTime = new Date().getTime() - start;
					$.ajax({url: "http://artistrecommender-priest.rhcloud.com/rest/subartists/"+response.id+""})
					.done(function (subbed) {
					for(i = 0; i<data.length;i++)
					if(subbed.indexOf(data[i].facebookId) >= 0) alist += "<li><a href=\"index.html?id="+data[i].facebookId+"\"><i class=\"fa fa-caret-right\"></i> "+data[i].name+"</a></li>";
				
					$.ajax({url: "loaded.html"}).done(function (html) {
						$("#mainCnt").html(html);
						
						id = url.substring(url.indexOf("id=")+"id=".length, url.length);
						sfid = id;
						$.ajax({url: "http://artistrecommender-priest.rhcloud.com/rest/artists/"+id+""})
						.done(function (data) {
							$("#allCnt").before("<div class=\"panel panel-default\"> \
									 <div class=\"panel-heading\"><h4>"+data.name+"</h4></div> \
									  <div class=\"panel-body\"> \
										<div><img align=\"left\" style=\"margin-right: 10px; margin-bottom: 10px;\" height=\"180px\" width=\"180px\" src=\"http://graph.facebook.com/"+data.facebookId+"/picture?width=720&height=720\"> \
										"+data.description+" \
										</div> \
										<div class=\"clearfix\"></div> \
									  </div> \
								   </div>");
						});
						$("#lg-menu").after(alist);
						getArtist(id+"/posts");
						getEvents(id);
						$("#fbLogginCheckTimeId").html("Checking facebook login takes: " +fbLogginCheckTime + " milliseconds");
						$("#serverReqTimeId").html("Server artists respone time: " +serverReqTime + " milliseconds");
					});
					})})});
					
				}
				else
					window.location.href = "http://www.kroljakub.pl/hack/SeusArtistas/index.html";
			}
		});
    } else if (response.status === 'not_authorized') {
		$('#loginModal').modal('show');
      // The person is logged into Facebook, but not your app.
    } else {
		$('#loginModal').modal('show');
		//$.ajax({url: "login.html"}).done(function (html) {$("#mainCnt").html(html);});
      // The person is not logged into Facebook, so we're not sure if
      // they are logged into this app or not.
    }
  }

  // This function is called when someone finishes with the Login
  // Button.  See the onlogin handler attached to it in the sample
  // code below.
  function checkLoginState() {
    FB.getLoginStatus(function(response) {
	  fbLogginCheckTimeStart = new Date().getTime();
      statusChangeCallback(response);
    });
  }

function subbToArtist(aid) {
	var formData = {userFacebookId:fid,artistFacebookId:aid};
	$.ajax({
    url : "http://artistrecommender-priest.rhcloud.com/rest/subartists",
    type: "POST",
    data : formData,
});
	setTimeout(function(){location.reload()},100);
}


function unsubbToArtist(aid) {
	var formData = {userFacebookId:fid,artistFacebookId:aid};
	$.ajax({
    url : "http://artistrecommender-priest.rhcloud.com/rest/subartists/delete",
    type: "POST",
    data : formData,
});
	setTimeout(function(){location.reload()},100);
}

function addPost(mgs) {
FB.api(sfid+'/feed', 
	"POST",
    {
        "message": document.getElementById("newPostArrea").value
    },
	function(response) {
	});
	setTimeout(function(){location.reload()},100);
}
function submitEvent() {
	eid = document.getElementById("submitEventInput").value;
	eid = eid.replace("https://www.facebook.com/events/", "")
	eid = eid.replace("http://www.facebook.com/events/", "")
	eid = eid.replace("/", "")
	
	var formData = {ArtistFacebookId:sfid, FacebookLink:eid};
	$.ajax({
    url : "http://artistrecommender-priest.rhcloud.com/rest/events",
    type: "POST",
    data : formData,
});
setTimeout(function(){location.reload()},1000);
}

function delEvent(id) {
	var formData = {ArtistFacebookId:sfid, FacebookLink:id};
	$.ajax({
    url : "http://artistrecommender-priest.rhcloud.com/rest/events/delete",
    type: "POST",
    data : formData,
});
setTimeout(function(){location.reload()},1000);
}

function addComment(postid) {
FB.api(postid+'/comments', 
	"POST",
    {
        "message": document.getElementById("inputV"+postid).value
    },
	function(response) {
	});
	setTimeout(function(){location.reload()},100);
}




  window.fbAsyncInit = function() {
  FB.init({
    appId      : '1445409792420544',
    cookie     : false,  // enable cookies to allow the server to access 
                        // the session
    xfbml      : true,  // parse social plugins on this page
    version    : 'v2.3' // use version 2.3
  });

  // Now that we've initialized the JavaScript SDK, we call 
  // FB.getLoginStatus().  This function gets the state of the
  // person visiting this page and can return one of three states to
  // the callback you provide.  They can be:
  //
  // 1. Logged into your app ('connected')
  // 2. Logged into Facebook, but not your app ('not_authorized')
  // 3. Not logged into Facebook and can't tell if they are logged into
  //    your app or not.
  //
  // These three cases are handled in the callback function.

  FB.getLoginStatus(function(response) {
	fbLogginCheckTimeStart = new Date().getTime();
    statusChangeCallback(response);
  });

  };

  // Load the SDK asynchronously
  (function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));

  // Here we run a very simple test of the Graph API after login is
  // successful.  See statusChangeCallback() for when this call is made.
  
  function getEvents(id) {
	  $.ajax({url: "http://artistrecommender-priest.rhcloud.com/rest/events/"+id})
		.done(function (evenets) {
			eventsHtml = "";
			for(i = 0; i<evenets.length;i++){
				FB.api(evenets[i], function(response) {

					eventsHtml += '<a href="https://www.facebook.com/events/'+response.id+'/"><li  class="list-group-item">'+response.name+'</a>';
					
					if(moder)
						eventsHtml += '<button type="submit" onclick="delEvent(\''+response.id+'\');" class="btn btn-default pull-right" style="color:black;margin-top:-7px ; margin-right:-10px;"><i class=\"fa fa-trash-o\"></i></button>';
					eventsHtml += '</li>';
					$("#eventsList").html(eventsHtml);
					});
				
			}
			
		});
  }
  
  
  function getArtist(id) {
  start = new Date().getTime();
  FB.api(id, function(response) {
	  getingPostTime = new Date().getTime()-start;
	  $("#getingPostTimeId").html("Getting post from facebook takes: " +getingPostTime + " milliseconds");
	  postsAll = "";
	  for(i = 0; i<response.data.length;i++){
		  if(response.data[i].message == undefined) continue;
		  img = response.data[i].picture;
		  if(img == undefined)
			  img = "http://cumbrianrun.co.uk/wp-content/uploads/2014/02/default-placeholder.png";
	   postsAll += '<div class="panel panel-default" >\
                                  <div class="panel-body"> \
                                    <img src="'+ img +'" class="img-circle pull-right">'+response.data[i].message+'\
                                    <div class="clearfix"></div> \
                                    <hr> ';
							if(response.data[i].comments != undefined)
							for(j = 0; j<response.data[i].comments.data.length;j++)
								postsAll+='<p><b>'+response.data[i].comments.data[j].from.name+':</b>'+response.data[i].comments.data[j].message+'</p>';
							postsAll+="<hr> \
                                    <div> \
                                    <div class=\"input-group\"> \
                                      <input id=\"inputV"+response.data[i].id+"\" type=\"text\" class=\"form-control\" placeholder=\"Add a comment..\"><div class=\"input-group-btn\"><button onclick=\"addComment('"+response.data[i].id+"');\" class=\"btn btn-default\"><i class=\"fa fa-comment\"></i></button> \
                                      </div>  \
                                    </div> \
                                    </div> \
                                  </div></div>";
	  }
	  $("#allPosts").html(postsAll);
  //document.getElementById('page').innerHTML = 'About: ' + response.about;
  //document.getElementById('cover').src = "http://graph.facebook.com/"+id+"/picture?width=720&height=720"
});
}