/*global $, alert, TweenMax, console */

$(function () {
	
	'use strict';
	/*------************-----------START--------****************-------*/
	$('#calcBMI').on('click', function () {
		var weight = $("#weight").val(),
			height = $("#height").val(),
			calcBMI = $("#calcBMI"),
			resultBMI = $("#resultBMI"),
			expV = Math.pow(height, 2),
			numValue = weight / expV;
		//console.log(numValue);
		$(resultBMI).empty().append('The Result: <span style="color:#dbed26; font-size: 130%;">' + numValue.toFixed(2));
			
		if (numValue < 18.5) {
			$('.bmi .td1').addClass('tr').fadeOut(200).fadeIn(200).fadeOut(200).fadeIn(200);
			$('.bmi .td2').removeClass('tr');
			$('.bmi .td3').removeClass('tr');
			$('.bmi .td4').removeClass('tr');
		} else if (numValue > 18.5 && numValue <= 24.9) {
			$('.bmi .td1').removeClass('tr');
			$('.bmi .td2').addClass('tr').fadeOut(200).fadeIn(200).fadeOut(200).fadeIn(200);
			$('.bmi .td3').removeClass('tr');
			$('.bmi .td4').removeClass('tr');
		} else if (numValue > 24.9 && numValue <= 29.9) {
			$('.bmi .td1').removeClass('tr');
			$('.bmi .td2').removeClass('tr');
			$('.bmi .td3').addClass('tr').fadeOut(200).fadeIn(200).fadeOut(200).fadeIn(200);
			$('.bmi .td4').removeClass('tr');
		} else if (numValue > 29.9) {
			$('.bmi .td1').removeClass('tr');
			$('.bmi .td2').removeClass('tr');
			$('.bmi .td3').removeClass('tr');
			$('.bmi .td4').addClass('tr').fadeOut(200).fadeIn(200).fadeOut(200).fadeIn(200);
		}
	});
	function cvdEstimateRisk() {
		// Calculate the resk factor of cvd
		var age = $('input[id=age]', '#cvdCalc').val(),
			smoke = $('input[name=smoke]:checked', '#cvdCalc').val(),
			cvd = $('input[name=cvd]:checked', '#cvdCalc').val(),
			sbp = $('#SBP').val(),
			tc = $('#TC').val(),
			ldl = $('#ldl-c').val(),
			gender = $('input[name=gender]:checked', '#cvdCalc').val(),
			ckd = $('input[name=ckd]:checked', '#cvdCalc').val(),
			sc = $('#SC').val(),
			diabetic = $('input[name=diabetic]:checked', '#cvdCalc').val(),
			albumin = $('#albumin').val(),
			preprandial = $('#preprandial').val(),
			postprandial = $('#postprandial').val(),
			a1c = $('#A1C').val(),
			gfrMale = Math.round(175 * Math.pow(sc, -1.154) * Math.pow(age, -0.203)).toFixed(2),
			gfrFemale = gfrMale * (0.742).toFixed(2),
			gfr = (gender === 'Male') ? gfrMale : gfrFemale;

		function patientData() {

			$("#sbpResult1").empty().append(sbp + ' mmHg');
			$("#sbpResult2").empty().append('130-139 mmHg');

			$("#tcResult1").empty().append(tc + ' mg/dl');
			$("#tcResult2").empty().append('<190 mg/dl');

			$("#ldlResult1").empty().append(ldl + ' mg/dl');
			$("#ldlResult2").empty().append('<70 mg/dl');

			$("#preResult1").empty().append(preprandial + ' mg/dl');
			$("#preResult2").empty().append('70-130 mg/dl');

			$("#postResult1").empty().append(postprandial + ' mg/dl');
			$("#postResult2").empty().append('<180 mg/dl');

			$("#a1cResult1").empty().append(a1c + ' %');
			$("#a1cResult2").empty().append('7% or less');

		} // End Function of Estimate Risk
		patientData();
		
		function score() {
			
			if (age <= 0 || gender === undefined || smoke === undefined) {
				
				$('#cvdResult1, #cvdResult2').empty().append('None');
				alert('Kindly put patient data: age, gender, smoke...'); // the first conditions
			} else if ((cvd || ckd === 'Yes') || (diabetic === 'Yes' && albumin > 30) || gfr < 30) {

				$('#cvdResult2').empty().append('<span style="color:red;font-wight:900; text-transform: uppercase;" class="vh">very high</span>');
				
				TweenMax.fromTo($('.vh'), 2, {'font-size': '50%', opacity: 0.2}, {'font-size': '100%', opacity: 1, repeat: -1, yoyo: true});
			} else if (((tc || sbp) === 'More...' && diabetic === 'Yes') ||  (gfr >= 30 && gfr < 60)) {

				$('#cvdResult2').empty().append('<span style="color:red;font-wight:bold; text-transform: uppercase;" class="h">high</span>');
				TweenMax.fromTo($('.h'), 2, {'font-size': '50%', opacity: 0.2}, {'font-size': '100%', opacity: 1, repeat: -1, yoyo: true});

			} else if (cvd === undefined && ckd === undefined && diabetic === undefined) {
				
				if ((cvd && ckd && diabetic) === 'No' || albumin <= 30) {
					
					if (gfr >= 60) {
						$("#gfrResult1").empty().append(gfr + ' mL/min/1.73m<sup>2</sup>');
						$("#gfrResult2").empty().append('Normal');
					}
					if (gender === 'Female') {

						if (smoke === 'No') {
							if (age <= 50) {
								if (sbp <= 140 && tc <= 150) {
									$('#cvdResult1').empty().append('0%');
									$('#cvdResult2').empty().append('Low');
								}
								if (sbp >= 141 && tc >= 151) {
									$('#cvdResult1').empty().append('1-2%');
									$('#cvdResult2').empty().append('Moderate');
								}
							} // end age 50

							if (age >= 51 && age <= 60) {
								if (sbp <= 140 && tc <= 150) {
									$('#cvdResult1').empty().append('1-4%');
									$('#cvdResult2').empty().append('Moderate');
								}
							} // end age 55

							if (age >= 56 && age <= 60) {
								if (sbp >= 161 && tc >= 251) {
									$('#cvdResult1').empty().append('5-7%');
									$('#cvdResult2').empty().append('High');
								}
							} // end age till 60

							if (age >= 61) {
								if (sbp <= 140 && tc <= 250) {
									$('#cvdResult1').empty().append('2-4%');
									$('#cvdResult2').empty().append('Moderate');
								}
								if ((sbp >= 141 && tc >= 251)) {
									$('#cvdResult1').empty().append('5-9%');
									$('#cvdResult2').empty().append('High');
								}
								if (sbp >= 180 && tc >= 250) {
									$('#cvdResult1').empty().append('10-12%');
									$('#cvdResult2').empty().append('Very High');
								}
							} // end age more than 60
						} // end smoke NO

						if (smoke === 'Yes') {
							if (age <= 40) {
								if (sbp <= 140 && tc <= 150) {
									$('#cvdResult1').empty().append('0%');
									$('#cvdResult2').empty().append('Low');
								}
							} // end age 40

							if (age >= 41 && age <= 50) {
								if (sbp >= 161 && tc <= 250) {
									$('#cvdResult1').empty().append('1-4%');
									$('#cvdResult2').empty().append('Moderate');
								}
							} // end age 50

							if (age >= 51 && age <= 55) {
								if (sbp <= 140 && tc <= 150) {
									$('#cvdResult1').empty().append('1-4%');
									$('#cvdResult2').empty().append('Moderate');
								}
							} // end age 60

							if (age >= 56 && age <= 60) {
								if (sbp <= 160 && tc <= 200) {
									$('#cvdResult1').empty().append('5-7%');
									$('#cvdResult2').empty().append('High');
								}
								if (sbp > 160 && tc > 200) {
									$('#cvdResult1').empty().append('10-13%');
									$('#cvdResult2').empty().append('Very High');
								}
							} // end age till 60

							if (age > 60) {
								if (sbp <= 140 && tc <= 250) {
									$('#cvdResult1').empty().append('5-9%');
									$('#cvdResult2').empty().append('High');
								}
								if (sbp >= 141 && tc >= 251) {
									$('#cvdResult1').empty().append('Very High');
									$('#cvdResult2').empty().append('Very High');
								}
							} // end age more than 60
						} // end smoke YES

					} // end gender FEMALE

					if (gender === 'Male') {

						if (smoke === 'No') {
							if (age <= 40) {
								if (sbp <= 140 && tc <= 150) {
									$('#cvdResult1').empty().append('0%');
									$('#cvdResult2').empty().append('Low');
								}
							} // end age 40

							if (age >= 41 && age <= 50) {
								if (sbp < 180 && tc < 200) {
									$('#cvdResult1').empty().append('1-4%');
									$('#cvdResult2').empty().append('Moderate');
								}
								if (sbp >= 180 && tc >= 250) {
									$('#cvdResult1').empty().append('5-7%');
									$('#cvdResult2').empty().append('High');
								}
							} // end age 50

							if (age >= 51 && age <= 55) {
								if (sbp <= 140 && tc <= 150) {
									$('#cvdResult1').empty().append('2-4%');
									$('#cvdResult2').empty().append('Moderate');
								}
								if (sbp >= 141 && tc >= 251) {
									$('#cvdResult1').empty().append('5-8%');
									$('#cvdResult2').empty().append('High');
								}
								if (sbp >= 180 && tc >= 250) {
									$('#cvdResult1').empty().append('10-12%');
									$('#cvdResult2').empty().append('Very High');
								}
							} // end age 55

							if (age >= 56 && age <= 60) {
								if (sbp <= 140 && tc <= 200) {
									$('#cvdResult1').empty().append('3-4%');
									$('#cvdResult2').empty().append('Moderate');
								}
								if (sbp > 160 && tc > 200) {
									$('#cvdResult1').empty().append('5-9%');
									$('#cvdResult2').empty().append('High');
								}
								if (sbp >= 180 && tc >= 200) {
									$('#cvdResult1').empty().append('10-15%');
									$('#cvdResult2').empty().append('Very High');
								}
							} // end age till 60

							if (age > 60) {
								if (sbp <= 140 && tc <= 250) {
									$('#cvdResult1').empty().append('5-9%');
									$('#cvdResult2').empty().append('High');
								}
								if (sbp >= 141 && tc >= 251) {
									$('#cvdResult1').empty().append('Very High');
									$('#cvdResult2').empty().append('Very High');
								}
							} // end age more than 60
						} // end smoke NO

						if (smoke === 'Yes') {
							if (age <= 40) {
								if (sbp > 120 && tc > 105) {
									$('#cvdResult1').empty().append('1-4%');
									$('#cvdResult2').empty().append('Moderate');
								}
							} // end age 40

							if (age >= 41 && age <= 50) {
								if (sbp <= 140 && tc <= 200) {
									$('#cvdResult1').empty().append('1-4%');
									$('#cvdResult2').empty().append('Moderate');
								}
								if (sbp > 140 && tc > 200) {
									$('#cvdResult1').empty().append('5-9%');
									$('#cvdResult2').empty().append('High');
								}
								if (sbp >= 180 && tc >= 250) {
									$('#cvdResult1').empty().append('10-14%');
									$('#cvdResult2').empty().append('Very High');
								}
							} // end age 50

							if (age >= 51 && age <= 55) {
								if (sbp <= 160 && tc <= 200) {
									$('#cvdResult1').empty().append('5-9%');
									$('#cvdResult2').empty().append('High');
								}
								if (sbp >= 161 && tc >= 201) {
									$('#cvdResult1').empty().append('Very High');
									$('#cvdResult2').empty().append('Very High');
								}
							} // end age 55

							if (age >= 56 && age <= 60) {
								if (sbp <= 120 && tc <= 200) {
									$('#cvdResult1').empty().append('6-9%');
									$('#cvdResult2').empty().append('High');
								}
								if (sbp >= 121 && tc >= 201) {
									$('#cvdResult1').empty().append('Very High');
									$('#cvdResult2').empty().append('Very High');
								}
							} // end age till 60

							if (age > 60) {
								if (sbp >= 120 && tc >= 150) {
									$('#cvdResult1').empty().append('Very High');
									$('#cvdResult2').empty().append('Very High');
								}
							} // end age more than 60
						} // end smoke YES

					} // end gender MALE condition
				}
			} // The main if condition ask for AGE & GENDER
			
		} // End Function Calc Score
		
		score();
		
		function gfrCalc() {
			if (gfr < 30) {
				$("#gfrResult1").empty().append(gfr + ' mL/min/1.73m<sup>2</sup>');
				$("#gfrResult2").empty().append('Severe chronic kidney disease');
			} else if (gfr > 29 && gfr < 60) {
				$("#gfrResult1").empty().append(gfr + ' mL/min/1.73m<sup>2</sup>');
				$("#gfrResult2").empty().append('Moderate chronic kidney disease');
			} else {
				$("#gfrResult1").empty().append(gfr + ' mL/min/1.73m<sup>2</sup>');
				$("#gfrResult2").empty().append('Normal');
				score();
			}
		}
		gfrCalc();
		
		console.log('Age: ' + age + '\n' + 'Gender: ' + gender + '\n' + 'Smoke: ' + smoke + '\n' + '\n' + 'CVD: ' + cvd + '\n' + 'CKD: ' + ckd + '\n' + 'Diabetic: ' + diabetic + '\n' + 'Total Chlosterol: ' + tc + '\n' + 'SBP: ' + sbp + '\n' + 'SC: ' + sc + '\n' + 'LDL-C: ' + ldl + '\n' + 'Albumin: ' + albumin + '\n' + 'pre: ' + preprandial + '\n' + 'post: ' + postprandial + '\n' + 'A1C: ' + a1c + '\n' + 'GFR: ' + gfr + '-' + gfrMale + '-' + gfrFemale);
	
	} // End cvdEstimateRisk function (Cardio_risk.html file)...
	
	
	$('#cvdCalc #estimate').on('click', cvdEstimateRisk);
	

	
}); // End of ready doc





