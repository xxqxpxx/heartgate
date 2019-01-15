/*global $, window, alert, TweenMax, console, document, FormData, AndroidFunction, FileReader, changeProfile, navigator, setTimeout, location, XMLHttpRequest, setInterval */

$(function () {
	
	'use strict';
	
	var isDevice = (/android|webos|iphone|ipad|ipod|blackberry/i.test(navigator.userAgent.toLowerCase())),
		interactionUp,
		interactionDown,
		interactionOver,
		interactionMove,
		userid,
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
        lookups,
        google;
	
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
// fill DDI select box
	function ddiOptions() {
        
        $.ajax({
            
            url: 'http://api.ddi.digitalcatsite.com/drugs_all',
            type: 'get',
            success: function (ddi) {
                //console.log(ddi);
                var ddiArr = JSON.parse(ddi),
                    i;

                //$('.ddiSpan .ui-select .ui-btn span').append("acebutolol");

                for (i in ddiArr) {
                    if (ddiArr.hasOwnProperty(i)) {
                        $('#sel1 optgroup').append('<option value="' + ddiArr[i].DRUG_ID + '">' + ddiArr[i].NAME + '</option>');
                        $('#sel2 optgroup').append('<option value="' + ddiArr[i].DRUG_ID + '">' + ddiArr[i].NAME + '</option>');
                        $('#sel3 optgroup').append('<option value="' + ddiArr[i].DRUG_ID + '">' + ddiArr[i].NAME + '</option>');
                    } // end if condition
                } // end for loop
            },
            processData: false,
            contentType: false
            
        }); // end AJAX
        
	} // end function ddiOptions()
	
	$(document).on(interactionUp, '#btnOfDDI', function () {
        setTimeout(function () {
            ddiOptions();
            $(this).fadeOut(200).fadeIn(200);
        }, 800);
        
    });
    
	ddiOptions();
    
	
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
       AndroidFunction.setUserId("0");
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
                
               AndroidFunction.setUserId(out.userdata[0].id);
               AndroidFunction.setUserName(out.userdata[0].username);
                
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
	
	userid = getCookie('userid');
   userid = AndroidFunction.getUserId();
    
	username = getCookie('username');
   username = AndroidFunction.getUserName();

    $('#resetpass').submit(function (event) {

        var email = $('#resetpass .form-group #emailaddress').val();

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
		$.getJSON('http://hg.api.digitalcatsite.com/users/current/' + userid, function (data) {
            
//			console.log(data[0].image_profile);
            
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
    
    $('#updateProfilePic').on(interactionUp, function () {
        
        AndroidFunction.changeProfile();
        
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
			url: "http://hg.api.digitalcatsite.com/users/update/" + userid,
			type: "POST",
			beforeSend: function () {
				//console.log('confirm here if data go to send!');
			},
			success: function (output) {
				if (output.state === "0") {
					$('body').append('<div class="containerAlert"><div class="alert"> <span class="closebtn" onclick="this.parentElement.parentElement.style.display=\'none\';">&times;</span>' + output.Message + '</div></div>');
					
				} else {
					$('body').append('<div class="containerAlert"><div class="alert" style="background-color: green">' + output.Message + '<br><hr><button type="button" class="btn btn-success btn-block" onclick="window.location.reload(true)">Done</button></div></div>');
				}
			},
			data: updateUserData,
			processData: false,  // tell jQuery not to process the data
			contentType: false   // tell jQuery not to set contentType
			
		});
		
		event.preventDefault();
		
	}); // end of submit update user info form  
	
	/*------******--------START GET NEAR BY INSIDE IF(x)--------*******-------*/// nearby data for current user
	function nearby() {
        
       AndroidFunction.nearBy();
        
	} // end nearby function
    
//	nearby();
	
	/*------********-----------START CONNECTIONS INSIDE IF(x)--------*******-------*/
	// Connections Section Show Connected users
    
	function currentConnected() {

		$('#myULc').empty();
                
		$.get('http://hg.api.digitalcatsite.com/connections/my_connections/' + userid, function (connections) {
            
			var i, myConnections = JSON.parse(connections);
                        
			for (i in myConnections) {
                
                if (myConnections.hasOwnProperty(i)) {
                    
                    if (myConnections[i].count > 0) {
                    
                        $('#myULc').append('<li id="userid' + myConnections[i].id + '"><span class="alertNote" style="z-index: 99; right: 13px;">' + myConnections[i].count + '</span>' +
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
                    } else {
                        
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
                    }
                    
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
		
		$.get('http://hg.api.digitalcatsite.com/connections/receive_connections/' + userid, function (myReceived) {
            
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
		
		$.get('http://hg.api.digitalcatsite.com/connections/sent_connections/' + userid, function (output) {
            var i, outputS = JSON.parse(output);
            console.log(outputS);

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
		$.get('http://hg.api.digitalcatsite.com/connections/find_connections/' + userid, function (allusers) {
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

        height += 2;

        $('#ChatAreaMsg').animate({scrollTop: height}, 300);
    }// end scrolling function
    
    function submitChat() {
        
        var msgText = $('#sendTextBox').val(),
            receiverUserId = $('#sendBtnText').attr('userid'),
            xmlhttp = new XMLHttpRequest();
        
        if (msgText === '') {
            
            alert('empty message!!!');
            
            return false;
            
        } else {
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {

                    document.getElementById('ChatAreaMsg').innerHTML = xmlhttp.responseText;
                    scrollingdown();

                }
            };
        }

        xmlhttp.open('GET', 'http://hg.api.digitalcatsite.com/api/insert_mobile.php?username=' + username + '&senduser=' + userid + '&msg=' + msgText + '&receiveuser=' + receiverUserId, true);
        xmlhttp.send();
                

    } // end submit Chat function
    
       
    // put the receiver id inside send button as attribute 'userid'
    $(document).on(interactionUp, '.messageBtn', function () {
        
        var receiverUserId = $(this).attr('userid'),
            wrapper = $('.wrapperMsg');
        
        wrapper.show().animate({left: 0}, 100, function () {
            setTimeout(scrollingdown, 2000);
        });
        
        $('#sendBtnText').attr('userid', receiverUserId);
                
    });
    
    $(document).on(interactionDown, '#sendBtnText', function () {
        submitChat();
        document.getElementById('sendTextBox').value = "";
        scrollingdown();
    }); // end send text button
    
    $('#sendTextBox').keypress(function (e) {
        if (e.keyCode === 13) {
            submitChat();
            scrollingdown();
            document.getElementById("sendBtnText").click();
            document.getElementById('sendTextBox').value = "";
            return false;
        }
        
    });
    $('#sendTextBox').focus(scrollingdown);

    //$.ajaxSetup({cache: false});
    
    setInterval(function () {
        var receiverUserId = $('#sendBtnText').attr('userid');
        $('#ChatAreaMsg').load('http://hg.api.digitalcatsite.com/api/logs_mobile.php?username=' + username + '&senduser=' + userid + '&receiveuser=' + receiverUserId);
    }, 2000);
    
    
    
    /* Add the new surveys here */
    $.get('http://hg.api.digitalcatsite.com/survey/' + userid, function (out) {
        var i, output = JSON.parse(out);
        
        console.log(output);
        
        if (output[0].count > 0) {
            
            $('#surveyNavIconPub').append('<span class="alertNote" style="top: -30px; right: 5px; z-index: 99">' + output[0].count + '</span>');
            $('#new-survey').attr('src', 'http://heartgate.co/survey/index.php?userid=' + userid);
            
        } else {
            $('#new-survey').empty();
            $('.surveyNavIconPub span').empty();
            $('#new-survey').attr('src', 'http://heartgate.co/survey/index.php?userid=' + userid);
        }
        
    });
    /* Add the new surveys here */
    $.get('http://hg.api.digitalcatsite.com/survey/all/' + userid, function (out) {
        var i, output = JSON.parse(out);
        
        console.log(output);
        
        for (i in output) {
            if (output.hasOwnProperty(i)) {
                
                $('#accordion').append(
                    '<div class="panel panel-default">' +
                        '<div class="panel-heading" style="background-color: rgba(251, 255, 157, 0.7); border-color: #914106; color: #994207; border-radius: 9px 0;">' +
                            '<h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" href="#' + output[i].id + '" survid="' + output[i].id + '" style="font-size: 1.3em; padding: 9px;">' + output[i].survey_name + '</h4>' +
                        '</div>' +
                        '<div id="' + output[i].id + '" class="panel-collapse collapse">' +
                            '<div class="panel-body">' +
                                '<iframe class="pri-survey" id="pri-survey" src="http://heartgate.co/survey/msurvey.php?userid=' + userid + '&survid=' + output[i].id + '" style="width: 100%; height: auto; min-height: 300px; padding: 0; border: none; margin-top: -10px"></iframe>' +
                            '</div>' +
                        '</div>' +
                        '</div>'
                );
                
            }
        } // end for loop
        
    });
    
    /* end the new surveys here */
    
    

    // handle connect Button with find connections
    $(document).on(interactionUp, '.connectBtn', function () {
        var data = {fk_userid_send: userid, fk_user_id_received: $(this).attr('userid'), create_user_id: username};
        
        $.post('http://hg.api.digitalcatsite.com/messages/connectuser/add', data, function (output) {
            console.log(output);
        });
        $(this).parent().parent().parent().hide(400);
    });

    // handle withdraw Button with sent section
    $(document).on(interactionUp, '.withdrawBtn', function () {
        var state_id = $(this).attr('state_id'),
            data = {fk_conn_state: $(this).attr('state_id')};
                
        $(this).fadeOut(200).fadeIn(200);

        $.post('http://hg.api.digitalcatsite.com/messages/connectuser/cancel/' + state_id, data, function (output) {
            //console.log(data); 
        });
        $(this).parent().parent().parent().hide(400);
    });

    // handle Approve Button with received section *****************************  .approve Button
    $(document).on(interactionUp, '.approveBtn', function () {
        var username = $(this).attr('id'),
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
        
//        console.log(data);

        $.post('http://hg.api.digitalcatsite.com/messages/connectuser/approve/' + state_id, data, function (out) {
//            console.log(out);
        
        });
        
        
        $(this).parent().parent().parent().hide(400);
    });




	
	/*------************-----------START--------****************-------*/
	// variables of notifications
    
	function alerts() {
                        
        $.get('http://hg.api.digitalcatsite.com/notifications/all/' + userid, function (out) {
            
            var messageCount = JSON.parse(out);
            
            if (messageCount[0].count > 0) {
                $('.myAccountNavIcon span').empty();
                $('.myAccountNavIcon').prepend('<span class="alertNote" style="right: 10; z-index: 99;">' + messageCount[0].count + '</span>');

                $('#connectionsLi span').empty();
                $('#connectionsLi').prepend('<span class="alertNote" style="right: 8px; top: 8px; z-index: 99;">' + messageCount[0].count + '</span>');

                $('#btnOfConnections span').empty();
                $('#btnOfConnections a').prepend('<span class="alertNote" style="left: 0px; top: -10px; z-index: 99;">' + messageCount[0].count + '</span>');
                
            } else {
                $('.myAccountNavIcon span').empty();
                $('#connectionsLi span').empty();
                $('#btnOfConnections span').empty();
                $('#surveyNavIconPub span').empty();
            }
            
        });
                        
        $.get('http://hg.api.digitalcatsite.com/notifications/calender/' + userid, function (out) {
            
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
    
    /* Handle Calendar Buttons ********************************************* */
    $(document).on(interactionUp, '.calendar-btns', function () {
        var state_id = parseInt($(this).attr('state_id'), 0),
            data = {fk_event_state_id: state_id};
        
        console.log(data);
                
        $.post('http://hg.api.digitalcatsite.com/calendar/btnState/' + userid, data, function (out) {
            
            var output = JSON.parse(out);
            
            console.log(output);
            
            if (output.state === "0") {
                alert('Done!');
            } else if (output.state === "1") {
                alert('Thanks for response.');
            }
        });
        
        $(this).attr('disabled', true).siblings().attr('disabled', false);
                
    });
    
    
    /* Get All News for Cardiovascular updates ******************************  */
    
    function cardioUpdatesNewNews() {
        $.get('http://hg.api.digitalcatsite.com/news/all/' + username, function (out) {
    //        console.log(output);
            var i, output = JSON.parse(out);
            for (i in output) {
                if (output.hasOwnProperty(i)) {
                    $('.addNews').append('<div><span class="title">' +
                                         output[i].title + '</span><br> <span class="links"><a href="' +
                                         output[i].link +
                                         '" target="blank">' +
                                         output[i].link +
                                         '</a></span></div>');
                }
            }

        });
    }
    
    function cardioUpdates() {

        $.get('http://hg.api.digitalcatsite.com/notifications/news/' + username, function (out) {
//            console.log(out);
            var i, output = JSON.parse(out);
//            console.log(output);
            for (i in output) {
                if (output.hasOwnProperty(i)) {
                    if (output.count > 0) {
                        $('.heartpressNavIcon a, #cardioIndexTitle').append('<span class="alertNote" style="top: 8px; right: 8px;">' + output.count + '</span>');
                    } else {
                        $('.heartpressNavIcon a span, #cardioIndexTitle span').empty();
                    }
                }
            }

        });
        
    } // end cardio updates function
	
    cardioUpdates();

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
        $('.wrapperMsg').animate({left: '-100%'}, 400).hide(setTimeout(500));
        //$(this).parent().parent().hide(400);
    });
    
    
	
    
    /* --------------------------start price page----------------------- */
    // handle price section
    function concorPrice() {
        $.get('http://hg.api.digitalcatsite.com/price', function (price) {
            
            var i, getAllPrice = JSON.parse(price);
            
//            console.log(getAllPrice);
            
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
	$('#cardioIndexTitle').on(interactionUp, cardioUpdatesNewNews);
	
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
