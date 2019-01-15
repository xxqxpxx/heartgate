/*global $, alert, TweenMax, console, document, FormData */

$(function () {
	
	'use strict';
	
	var isDevice = (/android|webos|iphone|ipad|ipod|blackberry/i.test(navigator.userAgent.toLowerCase())),
		interactionUp,
		interactionDown,
		interactionOver,
		interactionMove,
		x,
        username,
		sel1 = document.getElementById('sel1'),
		sel2 = document.getElementById('sel2'),
		sel3 = document.getElementById('sel3'),
		showRes = document.getElementById('showRes'),
		major = '<span class="major">Major</span><p>Highly clinically significant. Avoid combinations; the risk of the interaction outweighs the benefit.</p>',
		moderate = '<span class="moderate">Moderate</span><p>Moderately clinically significant. Usually avoid combinations; use it only under special circumstances.</p>',
		minor = '<span class="minor">Minor</span><p>Minimally clinically significant. Minimize risk; assess risk and consider an alternative drug, take steps to circumvent the interaction risk and/or institute a monitoring plan.</p>',
		noresult = '<span class="noresult">No result</span><p>No results found - however, this does not necessarily mean no interactions exist. Always consult with your doctor or pharmacist.</p>',
        gender,
        speciality,
        job,
        gov,
        experience,
        lookups;
	
	if (isDevice) {
		interactionUp = "touchend";
		interactionDown = "touchstart";
		interactionOver = interactionDown;
		interactionMove = 'touchmove';
	} else {
		interactionUp = "click";
		interactionDown = "mousedown";
		interactionOver = "mouseover";
		interactionMove = 'mousemove';

	}
	
	/*------************-----------START--------****************-------*/
	// make userid by create cookies
	function setCookie(cname, cvalue, exdays) {
		
		var d, expires;
		
		d = new Date();
		
		d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
		
		expires = "expires=" + d.toGMTString();
		
		document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
	}
		
	function getCookie(name) {
		var nameEQ = name + "=",
			ca = document.cookie.split(';'),
			i,
			c;
		
		for (i = 0; i < ca.length; i += 1) {
			c = ca[i];
			while (c.charAt(0) === ' ') {c = c.substring(1, c.length); }
			if (c.indexOf(nameEQ) === 0) {
				return c.substring(nameEQ.length, c.length);
			}
		}
		return null;
	}

	function eraseCookie(name) {
		setCookie(name, "", -1);
	}
	
	
	$(document).on(interactionUp, '#logoutProfileHome, #userLogout', function () {
		eraseCookie("userid");
		eraseCookie("username");
		eraseCookie("userMsgID");
        //AndroidFunction.setUserId("0");
		location.href = 'index.html';
	});
	
	
	
/*------************-----------START LOGIN Form--------****************-------*/
	// service of login form
    $('#login-form').submit(function (event) {
        //console.log('User Name: ' + $('#username').val());

        var username = $('#username').val(),
            password = $('#password').val(),
            url = "http://hg.api.digitalcatsite.com/login/" + username + "/" + password;
        //console.log(username);

        if (username === "" || password === "") {
            
            alert('Empty Values!!');
            
        }

        $.get(url, function (output) {
            
            var out = JSON.parse(output);
            
            //console.log(out.userdata[0].id);
            
            if (out.state === 0) {
                
                alert(out.Message);
                
            } else {
                
                setCookie('userid', out.userdata[0].id, 7);
                setCookie('username', out.userdata[0].username, 7);
                
                //AndroidFunction.setUserId(output);
                
                location.href = "home.html";
                
            }
        });

        event.preventDefault();
    });
    
    // Object contain user informations
    function GetUserInfo() {
        var username = getCookie('username'),
            userid = getCookie('userid');
        return {'username': username, 'userid': userid};
    }

/*------************-----------START Registeration Form--------****************-------*/
// service of register form
	$('#regForm').submit(function (event) {
		
		var regData, key;
						
		regData = new FormData(document.getElementById("regForm"));
        console.log(regData);
				
//		console.log(fullname);
//		
//		for (key of regData.keys()) {
//		   console.log(key); 
//		}
		
		$.ajax({
			url: "http://hg.api.digitalcatsite.com/users/add",
			type: "POST",
			beforeSend: function () {
				//console.log('confirm here if data go to send!');
			},
			success: function (output) {
                var outputJSON = JSON.parse(output);
				console.log(outputJSON);
				
				if (outputJSON.state === "0") {
					$('body').append('<div class="containerAlert"><div class="alert"> <span class="closebtn" onclick="this.parentElement.parentElement.style.display=\'none\';">&times;</span>' + outputJSON.Message + '</div></div>');
					
				} else {
					$('body').append('<div class="containerAlert"><div class="alert" style="background-color: green">' + outputJSON.Message + '<br><hr><button type="button" class="btn btn-success btn-block" onclick="location.href = \'index.html\'">Done</button></div></div>');
				}
			},
			data: regData,
			processData: false,  // tell jQuery not to process the data
			contentType: false   // tell jQuery not to set contentType
			
		});
		
		event.preventDefault();
		
	}); // end of submit register form
	
/*------********-----------START Record The USER ID--------************-------*/
// handle cookie and fill user profile
	
	x = getCookie('userid'); //|| AndroidFunction.getUserId();
    
	username = getCookie('username'); //|| AndroidFunction.getUserName();
    
    $('#forgotpass').on(interactionUp, function () {
        $('#resetPass').slideToggle();
    });

    $('#resetPass').submit(function (event) {

        var email = $('#resetPass .form-group #emailaddress').val();

        console.log(email);

        $.post('http://hg.api.digitalcatsite.com/users/reset/' + email, function (out) {
            console.log(out);
            alert(out);
        });

        event.preventDefault();
    });
    

    function getLookups() {
        
        $.getJSON('http://hg.api.digitalcatsite.com/lookups/gender', function (outs) {
            //console.log(outs);
            var i;
            for (i in outs) {
                if (outs.hasOwnProperty(i)) {
                    $('#fk_gender_id').append(
                        '<option value="' + outs[i].id + '">' + outs[i].name + '</option>'
                    );
                }
            }
            
        }); // end gender
        
        $.getJSON('http://hg.api.digitalcatsite.com/lookups/speciality', function (outs) {
            //console.log(outs);
            var i;
            for (i in outs) {
                if (outs.hasOwnProperty(i)) {
                    $('#fk_speciality_id').append(
                        '<option value="' + outs[i].id + '">' + outs[i].name + '</option>'
                    );
                }
            }
            
        }); // end speciality
        
        $.getJSON('http://hg.api.digitalcatsite.com/lookups/job', function (outs) {
            //console.log(outs);
            var i;
            for (i in outs) {
                if (outs.hasOwnProperty(i)) {
                    $('#fk_job_id').append(
                        '<option value="' + outs[i].id + '">' + outs[i].name + '</option>'
                    );
                }
            }
            
        }); // end job
        
        $.getJSON('http://hg.api.digitalcatsite.com/lookups/gov', function (outs) {
//            console.log(outs);
            var i;
            for (i in outs) {
                if (outs.hasOwnProperty(i)) {
                    $('#fk_current_living_place, #fk_current_work_place, #fk_previous_work_place').append(
                        '<option value="' + outs[i].id + '">' + outs[i].name + '</option>'
                    );
                }
            }
            
        }); // end gov
        
        $.getJSON('http://hg.api.digitalcatsite.com/lookups/experience', function (outs) {
//            console.log(outs);
            var i;
            for (i in outs) {
                if (outs.hasOwnProperty(i)) {
                    $('#fk_year_of_experience').append(
                        '<option value="' + outs[i].id + '">' + outs[i].name + '</option>'
                    );
                }
            }
            
        }); // end experience
        
    } // end lookup function
    
    getLookups();
    
    
	function homeAndProfileData() {
	/*------********------START Fill USER PROFILE INSIDE IF(x)---********-------*/
		// profile data of current user
		$.getJSON('http://hg.api.digitalcatsite.com/users/current/' + x, function (data) {
            
			//console.log(data);
            
            var fullname = data[0].fullname,
                res = fullname.split(' ');
			
			$('#fullNameOnHomePage').empty().append('<span style="color: darkblue;"> ' + data[0].fullname + '</span>');
            $('#emailOnHomePage').empty().append('<span style="color: darkblue;"> ' + data[0].email + '</span>');
            $('#picOnHomePage').attr('src', 'http://assets.hg.api.digitalcatsite.com/' + data[0].image_profile);
            
            $('#image_profile').attr('src', 'http://assets.hg.api.digitalcatsite.com/' + data[0].image_profile);
            
			$('#updateUserInfo .form-group #firstname').val(res[0]);
			$('#updateUserInfo .form-group #midname').val(res[1]);
			$('#updateUserInfo .form-group #lastname').val(res[2]);
			$('#updateUserInfo .form-group #username').val(data[0].username);
			$('#updateUserInfo .form-group #email').val(data[0].email);
			$('#updateUserInfo .form-group #mobile_number').val(data[0].mobile_number);
			$('#updateUserInfo .form-group #birthdate').val(data[0].birthdate);
			$('#updateUserInfo .form-group #fk_gender_id').val(data[0].fk_gender_id);
			$('#updateUserInfo .form-group #fk_speciality_id').val(data[0].fk_speciality_id);
			$('#updateUserInfo .form-group #fk_job_id').val(data[0].fk_job_id);
			$('#updateUserInfo .form-group #fk_current_living_place').val(data[0].fk_current_living_place);
			$('#updateUserInfo .form-group #fk_current_work_place').val(data[0].fk_current_work_place);
			$('#updateUserInfo .form-group #fk_previous_work_place').val(data[0].fk_previous_work_place);
			$('#updateUserInfo .form-group #fk_year_of_experience').val(data[0].fk_year_of_experience);
			$('#updateUserInfo .form-group #interests').val(data[0].interests);
			
		});
        
        
	} // End function homeAndProfileData
	
	homeAndProfileData();
    
    $('#updateProfilePic').submit(function () {
        
        var image_profile = $("#image_profile").attr('title'),
            data = {image_profile: image_profile};
        
        console.log(data);

        $.post('http://hg.api.digitalcatsite.com/users/update_imageprofile_ios/' + x, data, function (out) {
            //console.log(out);
            window.location.reload(true);
        });
        
    });
    
    function chPassword() {
    
        var elemDisplay = $("#password-form").css('height');
        
        if (elemDisplay <= '1px') {
            
            $("#password-form").animate({height: '90px', overflow: 'visible', opacity: 1});

        } else {
            
            $("#password-form").animate({height: '1px', overflow: 'hidden', opacity: 0});
        }
    }
    
    $(document).on(interactionUp, '#checkboxid', chPassword);
    
/*------************-----------START Update User Form--------****************-------*/
// service of update form
	$('#updateUserInfo').submit(function (event) {
		
		var updateUserData, key;
						
		updateUserData = new FormData(document.getElementById("updateUserInfo"));
        
        console.log(updateUserData);
        
		$.ajax({
			url: "http://hg.api.digitalcatsite.com/users/update/" + x,
			type: "POST",
			beforeSend: function () {
				//console.log('confirm here if data go to send!');
			},
			success: function (output) {
                console.log(output);
                window.location.reload(true);
//				
//				if (outputJSON.state === "0") {
//					$('body').append('<div class="containerAlert"><div class="alert"> <span class="closebtn" onclick="this.parentElement.parentElement.style.display=\'none\';">&times;</span>' + outputJSON.Message + '</div></div>');
//					
//				} else {
//					$('body').append('<div class="containerAlert"><div class="alert" style="background-color: green">' + outputJSON.Message + '<br><hr><button type="button" class="btn btn-success btn-block" onclick="location.href = \'index.html\'">Done</button></div></div>');
//				}
			},
			data: updateUserData,
			processData: false,  // tell jQuery not to process the data
			contentType: false   // tell jQuery not to set contentType
			
		});
		
		event.preventDefault();
		
	}); // end of submit update user info form  
    
    
    $(document).on(interactionUp, '.backBtnNavTop', function () {
        //alert();
        location.href = "home.html";
    });
	
	/*------******--------START GET NEAR BY INSIDE IF(x)--------*******-------*/// nearby data for current user
	function nearby() {
		// nearby data for current user
		$.get('http://hg.api.digitalcatsite.com/nearby_drs/' + x, function (nearbyData) {
			
            var i, nearby = JSON.parse(nearbyData);
            
            
            //console.log(nearby[0]);
			//alert(data[0].id);
			$('#containerNearBy').empty();

            for (i in nearby) {
				if (nearby.hasOwnProperty(i)) {
					$('#containerNearBy').append(
						'<div class="col-xs-12 col-lg-4 col-md-3">' +
							'<div class="panel panel-default">' +
							'<div class="panel-body contacts-box">' +
							'<div class="findNearBy">' +
							'<div class="usrpic">' +
							'<img src="http://assets.hg.api.digitalcatsite.com/' + nearby[i].image_profile + '" height="75">' +
							'</div><div class="usrname">' + nearby[i].fullname +
							'</div><div class"homeloc">' + nearby[i].speciality +
							'</div></div></div></div></div>'
					);
				}
			}
		});
	} // end nearby function
    
	nearby();
	
	/*------********-----------START CONNECTIONS INSIDE IF(x)--------*******-------*/
	// Connections Section Show Connected users
    
    function notificationsOfMsg(receiverId) {
        
        return {
            messageCount: $.get('http://hg.api.digitalcatsite.com/notifications/locate/' + x + '/' + receiverId, function (out) {
                var msgs = JSON.parse(out);
            })
        };
        
    }
    
//    console.log(notificationsOfMsg(287));
    
	function currentConnected() {

		$('#myULc').empty();
                
		$.get('http://hg.api.digitalcatsite.com/connections/my_connections/' + x, function (connections) {
            
			var i, myConnections = JSON.parse(connections);
                        
			for (i in myConnections) {
                
                if (myConnections.hasOwnProperty(i)) {
                    
                    $('#myULc').append('<li id="userid' + myConnections[i].id + '">' +
                                '<a class="row">' +
                                    '<div class="col-xs-2">' +
                                        '<img src="http://assets.hg.api.digitalcatsite.com/' + myConnections[i].image_profile + '" alt="">' +
                                    '</div>' +
                                    '<div class="col-xs-7">' +
                                        '<span>' + myConnections[i].fullname + '</span><br>' +
                                        '<span>' + myConnections[i].speciality + '</span>' +
                                    '</div>' +
                                    '<div class="col-xs-3 text-center">' +
                                        '<button id="' + myConnections[i].username + '" userid="' + myConnections[i].id + '" class="btn btn-info messageBtn">Message</button>' +
                                    '</div>' +

                                '</a>' +
                            '</li>');
                    
                } // end if condetion
                
            } // end loop
            
        }); // end service My Connected
        
		
	} // end current connections function
    
    currentConnected();
	
	$(document).on(interactionUp, '#getMyConnections', function () {
		currentConnected();
		$(this).fadeOut(200).fadeIn(200);
    });
		
	//  Connections Section List of received
	function currentReceieved() {
		
		$('#myULr').empty();
		
		$.get('http://hg.api.digitalcatsite.com/connections/receive_connections/' + x, function (myReceived) {
            
            var i, outputR = JSON.parse(myReceived);
            
            //console.log('list of received Array: ' + myReceived);
            
			for (i in outputR) {
				if (outputR.hasOwnProperty(i)) {
                    $('#myULr').append('<li id="userid' + outputR[i].id + '">' +
                                    '<a class="row">' +
                                        '<div class="col-xs-2">' +
                                            '<img src="http://assets.hg.api.digitalcatsite.com/' + outputR[i].image_profile + '" alt="">' +
                                        '</div>' +
                                        '<div class="col-xs-7">' +
                                            '<span>' + outputR[i].fullname + '</span><br>' +
                                            '<span>' + outputR[i].speciality + '</span>' +
                                        '</div>' +
                                        '<div class="col-xs-3">' +
                                            '<button id="' + outputR[i].username + '" userid="' + outputR[i].id + '" state_id="' + outputR[i].state_id + '" class="btn btn-success approveBtn">Approve</button>' +
                                            '<button id="' + outputR[i].username + '" userid="' + outputR[i].id + '" state_id="' + outputR[i].state_id + '" class="btn btn-danger rejectBtn">Reject</button>' +
                                        '</div>' +
                                    '</a>' +
                                '</li>');
                    
				} // end main if
			} // end loop
		}); // end Received Service
		
	} // end glob receive function
    
    currentReceieved();
        
	$(document).on(interactionUp, '#getReceivedConnections', function () {
		currentReceieved();
		$(this).fadeOut(200).fadeIn(200);
	});
	
	//  Connections Section List of Sent users
	function currentSentRequested() {
		
		$('#myULs').empty();
		
		$.get('http://hg.api.digitalcatsite.com/connections/sent_connections/' + x, function (output) {
            var i, outputS = JSON.parse(output);
            //console.log(outputS);

            for (i in outputS) {
				if (outputS.hasOwnProperty(i)) {
					$('#myULs').append('<li id="userid' + outputS[i].id + '">' +
                                    '<a class="row">' +
                                        '<div class="col-xs-2">' +
                                            '<img src="http://assets.hg.api.digitalcatsite.com/' + outputS[i].image_profile + '" alt="">' +
                                        '</div>' +
                                        '<div class="col-xs-7">' +
                                            '<span>' + outputS[i].fullname + '</span><br>' +
                                            '<span>' + outputS[i].speciality + '</span>' +
                                        '</div>' +
                                        '<div class="col-xs-3 text-center">' +
                                            '<button id="' + outputS[i].username + '" userid="' + outputS[i].id + '" state_id="' + outputS[i].state_id + '" class="btn btn-info withdrawBtn">Withdraw</button>' +
                                        '</div>' +
                                    '</a>' +
                                '</li>');
				} // end if
			} // end loop
		});
	} // end currentSentRequested
    
    currentSentRequested();
    
	$(document).on(interactionUp, '#getSentConnections', function () {
		currentSentRequested();
		$(this).fadeOut(200).fadeIn(200);
	});
	
	// connections get all users for Find Connections
	function findDoctors() {
		$.get('http://hg.api.digitalcatsite.com/connections/find_connections/' + x, function (allusers) {
            var i, users = JSON.parse(allusers);
            
            //console.log(users);
            
			$('#myULfd').empty();
			for (i in users) {
				if (users.hasOwnProperty(i)) {
                    $('#myULfd').append('<li id="userid' + users[i].id + '">' +
                                '<a class="row">' +
                                    '<div class="col-xs-2">' +
                                        '<img src="http://assets.hg.api.digitalcatsite.com/' + users[i].image_profile + '" alt="">' +
                                    '</div>' +
                                    '<div class="col-xs-7">' +
                                        '<span>' + users[i].fullname + '</span><br>' +
                                        '<span>' + users[i].speciality + '</span>' +
                                    '</div>' +
                                    '<div class="col-xs-3 text-center">' +
                                        '<button id="userid' + users[i].id + '" userid="' + users[i].id + '" class="btn btn-primary connectBtn">Connect</button>' +
                                    '</div>' +
                                '</a>' +
                            '</li>');
				} // end if parent
			} // end loop
		});
	} // end function which get all doctors
    
	$(document).on(interactionUp, '#getAllConnections', function () {
		findDoctors();
		$(this).fadeOut(200).fadeIn(200);
	});
	
                
    // handle message Button with connections	************   get All Messages
    
    function scrollingdown() {
        var height = 0;
        $('#ChatAreaMsg div').each(function (i, value) {
            height += parseInt($(this).height(), 0);
        });

        height += '';

        $('#ChatAreaMsg').animate({scrollTop: height});
    }
    
    function getAllMessages(receiverUserId) {
        
        var myHours, myMinutes, d, C, sendUser = new GetUserInfo(),
            sendUserName = sendUser.username,
            sendUserId = sendUser.userid;
        
        $.ajax({
            url: 'http://hg.api.digitalcatsite.com/messages/' + sendUserId + '/' + receiverUserId,
            type: 'get',
            success: function (outdata) {

                $('#ChatAreaMsg').empty();

                //console.log(outdata);

                var i, msgs = JSON.parse(outdata);
                d = new Date();
                myHours = d.getHours();
                myMinutes = d.getMinutes();
                myMinutes = myMinutes + " ";
                C += 1;

                console.log(msgs);

                for (i in msgs) {
                    if (msgs.hasOwnProperty(i)) {

                        if (msgs[i].username === sendUserName) { //document.getElementById('sendTextBox').value !== ""

                            if (myMinutes.length === 1) {
                                myMinutes = "0" + myMinutes;
                            }
                            $('#ChatAreaMsg').append('<div style="width: 100%; height: auto; display: inline-block;">' +
                                '<div class="baloon" style="float: right; border-bottom: 5px solid #008141; border-radius: 50% 0 50% 50%; text-align: -webkit-auto;"><span>' +
                                msgs[i].userMessage + '</span><br><div style="float: bottom; font-size: 12px; padding: 9px; margin-bottom: 1px;">' +
                                myHours + ':' + myMinutes + '</div></div></div>');

                        } else {
                            $('#ChatAreaMsg').append('<div style="width: 100%; height: auto; display: inline-block;">' +
                                '<div class="baloon" style="float: left; border-bottom: 5px solid #008141; border-radius: 0 50% 50% 50%; text-align: -webkit-auto;"><span>' +
                                msgs[i].userMessage + '</span><br><div style="float: bottom; font-size: 12px; padding: 9px; margin-bottom: 1px;">' +
                                myHours + ':' + myMinutes + '</div></div></div>');
                        }
                    } // end if condition
                } // end loops
            },
            processData: false,
            contentType: false
        });
                    
        document.getElementById('sendTextBox').value = "";
                
    } // End get All Messages Function
    
    $(document).on(interactionUp, '#sendBtnText', function (e) {
                    
        var msgText = $('#sendTextBox').val(),
            user = new GetUserInfo(),
            receiverUserId = $('#sendBtnText').attr('userid'),
            userid = parseInt(user.userid, 0),
            data = {fk_userid_send: x, fk_userid_received: receiverUserId, message: msgText, create_user_id: user.username};

        //console.log(data);

        $.post('http://hg.api.digitalcatsite.com/messages/add', data, function (out) {
            console.log(data);
        });

        //$('#sendTextBox').val('');

        setTimeout(getAllMessages(receiverUserId), 400);
        
                
    }); // end send text button
    function sendTheMessage() {
        
        var msgText = $('#sendTextBox').val(),
            user = new GetUserInfo(),
            receiverUserId = $('#sendBtnText').attr('userid'),
            userid = parseInt(user.userid, 0),
            data = {fk_userid_send: x, fk_userid_received: receiverUserId, message: msgText, create_user_id: user.username};

        //console.log(data);

        $.post('http://hg.api.digitalcatsite.com/messages/add', data, function (out) {
            console.log(data);
        });

        //$('#sendTextBox').val('');

        setTimeout(getAllMessages(receiverUserId), 50);
        
                
    } // end send the message function
    
    $(document).on(interactionUp, '#sendBtnText', function () {
        sendTheMessage();
        scrollingdown();
    }); // end send text button
    
    $(document).on(interactionUp, '#sendTextBox', function () {
        sendTheMessage();
        scrollingdown();
    }); // end send text box
    
    document.getElementById("sendTextBox")
        .addEventListener("keyup", function (event) {
            event.preventDefault();
            if (event.keyCode === 13) {
                document.getElementById("sendBtnText").click();
                scrollingdown();
            }
        });
    
//    $('#sendTextBox').keypress(function (e) {
//        
//        if (e.keyCode === 13) {
//            sendTheMessage();
//            scrollingdown();
//            return false;
//        }
//        
//    });
    
//    function myFunction(e) {
//            
//        if (e.keyCode === 13) {
//            e.preventDefault;
//        }
//        return false;
//    }
    
    $(document).on(interactionUp, '.messageBtn', function () {
        
        var setFunc, receiverUserId = $(this).attr('userid'),
            wrapper = $('.wrapperMsg');
        
        wrapper.show();
        
        $('#sendBtnText').attr('userid', receiverUserId);
        
        getAllMessages(receiverUserId);
        
    });
    

    // handle connect Button with find connections
    $(document).on(interactionUp, '.connectBtn', function () {
        var userInfo = new GetUserInfo(),
            data = {fk_userid_send: x, fk_user_id_received: $(this).attr('userid'), create_user_id: userInfo.username};
        
        $.post('http://hg.api.digitalcatsite.com/messages/connectuser/add', data, function (output) {
            //console.log(output);
        });
        $(this).parent().parent().parent().hide(400);
    });

    // handle withdraw Button with sent section
    $(document).on(interactionUp, '.withdrawBtn', function () {
        var state_id = $(this).attr('state_id');
                
        $(this).fadeOut(200).fadeIn(200);

        $.get('http://hg.api.digitalcatsite.com/messages/connectuser/cancel/' + state_id, function (output) {
            //console.log(data); 
        });
        $(this).parent().parent().parent().hide(400);
    });

    // handle Approve Button with received section *****************************  .approve Button
    $(document).on(interactionUp, '.approveBtn', function () {
        var newUser = new GetUserInfo(),
            username = $(this).attr('id'),
            state_id = $(this).attr('state_id'),
            data = {update_user_id: username, fk_conn_state: 2};
        
        console.log(data);

        $.post('http://hg.api.digitalcatsite.com/messages/connectuser/approve/' + state_id, data, function (out) {
            console.log(out);
        
        });
        
        $(this).parent().parent().parent().hide(400);
        
    });

    // handle reject Button with received section **************************** .reject Button
    $(document).on(interactionUp, '.rejectBtn', function () {
        var state_id = $(this).attr('state_id'),
            username = $(this).attr('id'),
            data = {update_user_id: username, fk_conn_state: 3};
        
        console.log(data);

        $.post('http://hg.api.digitalcatsite.com/messages/connectuser/approve/' + state_id, data, function (out) {
            console.log(out);
        
        });
        
        
        $(this).parent().parent().parent().hide(400);
    });



/*------************-----------START--------****************-------*/
// fill DDI select box
	function ddiOptions() {
        
		var ddiArr = ["benazepril", "captopril", "enalapril", "fosinopril", "lisinopril", "moexipril", "perindopril", "quinapril", "ramipril", "trandolapril", "candesartan", "eprosartan", "irbesartan", "losartan", "telmisartan", "valsartan", "acebutolol", "atenolol", "betaxolol", "bisoprolol", "metoprolol", "nadolol", "propranolol", "sotalol", "amlodipine", "diltiazem", "felodipine", "nifedipine", "nimodipine", "nisoldipine", "verapamil", "amiloride", "bumetanide", "chlorothiazide", "chlorthalidone", "furosemide", "hydrochlorothiazide", "indapamide", "spironolactone", "aspirin", "clopidogrel", "rivaroxaban", "dabigatran", "apixaban", "warfarin", "isosorbide dinitrate", "nesiritide", "hydralazine", "minoxidil", "digoxin", "rosuvastatin", "atorvastatin", "lovastatin", "niacin", "simvastatin", "ezetimibe", "ranitidine", "omeprazole", "levothyroxine", "diclofenac", "paracetamol", "metformin", "glimepiride", "nitroglycerin", "ampicillin", "ciprofloxacin", "sitagliptin", "pregabalin", "insulin glargine", "esomeprazole", "albuterol", "pentoxifylline", "allopurinol", "ondansetron", "ketoprofen", "ibuprofen"],
			i,
            ddiSort;
        
        ddiSort = ddiArr.sort();
        
        //$('.ddiSpan .ui-select .ui-btn span').append("acebutolol");
	   
		for (i in ddiSort) {
            if (ddiSort.hasOwnProperty(i)) {
                $('#sel1 optgroup').append('<option>' + ddiSort[i] + '</option>');
                $('#sel2 optgroup').append('<option>' + ddiSort[i] + '</option>');
                $('#sel3 optgroup').append('<option>' + ddiSort[i] + '</option>');
            } // end if condition
		} // end for loop
        
        
        
	} // end function ddiOptions()
	
	$(document).on(interactionUp, '#btnOfDDI', function () {
        setTimeout(function () {
            ddiOptions();
            $(this).fadeOut(200).fadeIn(200);
        }, 800);
        
    });
    
	ddiOptions();
    
	
	/*------************-----------START--------****************-------*/
	// variables of notifications
    
	function alerts() {
                        
        $.get('http://hg.api.digitalcatsite.com/notifications/all/' + x, function (out) {
            
            var messageCount = JSON.parse(out);
            
            if (messageCount[0].count > 0) {
                $('.myAccountNavIcon span').empty();
                $('.myAccountNavIcon').prepend('<span class="alertNote" style="right: 0">' + messageCount[0].count + '</span>');

                $('#connectionsLi span').empty();
                $('#connectionsLi').prepend('<span class="alertNote" style="right: 8px; top: 8px">' + messageCount[0].count + '</span>');

                $('#btnOfConnections span').empty();
                $('#btnOfConnections a').append('<span class="alertNote" style="top: -8px; right: -8px;">' + messageCount[0].count + '</span>');
            } else {
                $('.myAccountNavIcon span').empty();
                $('#connectionsLi span').empty();
                $('#btnOfConnections span').empty();
            }
            
        });
                        
//        $.get('http://hg.api.digitalcatsite.com/notifications/locate/' + x + '/' + receiverId, function (out) {
//            
//            var messageCount = JSON.parse(out);
//            
//            if (messageCount[0].count > 0) {
//                $('#myULc li a span.alertNote').empty();
//                $('.#myULc li a').append('<span class="alertNote" style="right: 0">' + messageCount[0].count + '</span>');
//
//            } else {
//                $('#myULc li a span.alertNote').empty();
//            }
//            
//        });
                        
        $.get('http://hg.api.digitalcatsite.com/notifications/calender/' + x, function (out) {
            
            var eventsCount = JSON.parse(out);
            
            if (eventsCount[0].count > 0) {
                $('#calLi span').empty();
                $('#calLi').append('<span class="alertNote" style="top: 8px; right: 8px;">' + eventsCount[0].count + '</span>');
            } else {
                $('#calLi span').empty();
            }
            
        });
        
    } // end notifications functions
    
    alerts();
	
	


    /* ----------- Search function for doctors ---------------- */
    function searchDoctors(inputz, ULz) {
        // Declare variables
        var input, filter, ul, li, a, i;
        input = document.getElementById(inputz);
        filter = input.value.toUpperCase();
        ul = document.getElementById(ULz);
        li = ul.getElementsByTagName('li');

        // Loop through all list items, and hide those who don't match the search query
        for (i = 0; i < li.length; i += 1) {
            a = li[i].getElementsByTagName("a")[0];
            if (a.innerHTML.toUpperCase().indexOf(filter) > -1) {
                li[i].style.display = "";
            } else {
                li[i].style.display = "none";
            }
        }
    }
    
    $('input#inputc').keyup(function () {
        searchDoctors('inputc', 'myULc');
        //this.fadeOut(300).fadeIn(300);
    });
    
    $('input#inputr').keyup(function () {
        searchDoctors('inputr', 'myULr');
        //this.fadeOut(300).fadeIn(300);
    });
    
    $('input#inputs').keyup(function () {
        searchDoctors('inputs', 'myULs');
        //this.fadeOut(300).fadeIn(300);
    });
    
    $('input#inputfd').keyup(function () {
        searchDoctors('inputfd', 'myULfd');
        //this.fadeOut(300).fadeIn(300);
    });
    
    function sortList() {
        var list, i, switching, b, shouldSwitch;
        list = document.getElementById("myUL");
        switching = true;
        /*Make a loop that will continue until
        no switching has been done:*/
        while (switching) {
            //start by saying: no switching is done:
            switching = false;
            b = list.getElementsByTagName("LI");
            //Loop through all list items:
            for (i = 0; i < (b.length - 1); i += 1) {
                //start by saying there should be no switching:
                shouldSwitch = false;
                /*check if the next item should
                switch place with the current item:*/
                if (b[i].innerHTML.toLowerCase() > b[i + 1].innerHTML.toLowerCase()) {
                /*if next item is alphabetically lower than current item,
                mark as a switch and break the loop:*/
                    shouldSwitch = true;
                    break;
                }
            }
            if (shouldSwitch) {
                /*If a switch has been marked, make the switch
                and mark the switch as done:*/
                b[i].parentNode.insertBefore(b[i + 1], b[i]);
                switching = true;
            }
        }
    }
	
    
    /* --------------------------start message section----------------------- */
    // handle reject Button with received section
    $(document).on(interactionUp, '.wrapperMsg .headerMsg i', function () {
        $(this).parent().parent().hide(400);
    });
    
    
	
    
    /* --------------------------start price page----------------------- */
    // handle price section
    function concorPrice() {
        $.get('http://hg.api.digitalcatsite.com/price', function (price) {
            
            var i, getAllPrice = JSON.parse(price);
            
            //console.log(getAllPrice);
            
            $('#tableOfPrice').empty();
                        
            for (i in getAllPrice) {
                
                if (getAllPrice.hasOwnProperty(i)) {
                    $('#tableOfPrice').append(
                        '<tr><td>' + getAllPrice[i].concor + '</td><td>' + getAllPrice[i].price + '</td></tr>'
                    );
                }
            }
                        
        });
    }
    concorPrice();
    
    
	/* handle click of buttons navigation */
	
	$('#nearbyLi').on(interactionUp, nearby);
	$('#connectionsLi').on(interactionUp, currentConnected);
	$('#favLi').on(interactionUp, homeAndProfileData);
	$('#calLi').on(interactionUp, homeAndProfileData);
	
	$('#homeBtn').on(interactionUp, function () {
		// profile data of current user
		
		setTimeout(function () {
			location.reload();
		}, 1000);
		
		$(this).fadeOut(200).fadeIn(200);
	});
	
	$(document).on(interactionUp, '#btnOfHome', homeAndProfileData).fadeOut(200).fadeIn(200);
	$(document).on(interactionUp, '#btnOfConnections', currentConnected).fadeOut(200).fadeIn(200);
	$(document).on(interactionUp, '#btnOfNearby', nearby).fadeOut(200).fadeIn(200);
	$(document).on(interactionUp, '#btnOfPrice', concorPrice).fadeOut(200).fadeIn(200);
	$(document).on(interactionUp, '#btnOfDDI', ddiOptions).fadeOut(200).fadeIn(200);
	
	$(document).on(interactionUp, '.fa-refresh', function () {
		
		TweenMax.fromTo($('.fa-refresh'), 0.5, {
			opacity: '.1',
			rotation: '-360',
			scaleX: '.7',
			scaleY: '.7'
		}, {
			opacity: 1,
			rotation: 0,
			scale: 1,
            onComplete: function () {
                currentConnected();
                window.location.reload(true);
            }
		});
        
	});
    
    $('.fa-refresh').css({
        'z-index': 9999999,
        'position': 'absolute',
        'right': '21%',
        'top': '15px'
    });
    
	
}); // End ready documney function
