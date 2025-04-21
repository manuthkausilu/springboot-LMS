$(document).ready(function() {
    const BASE_URL = 'http://localhost:8080';
    var accessToken = localStorage.getItem('userToken');
    var userId = localStorage.getItem('userId');

    // Function to load courses (unchanged)
    function loadCourses() {
        $.ajax({
            url: `${BASE_URL}/api/v1/courses/all`,
            method: 'GET',
            dataType: 'json',
            success: function(response) {
                const coursesContainer = $('#courses-container');
                coursesContainer.empty();

                response.courseList.forEach(function(course) {
                    const courseCard = `
                        <div class="col-md-4">
                            <div class="card course-card">
                                <img src="http://localhost:8080${course.imgUrl}" class="card-img-top course-image" alt="${course.title}">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <h5 class="card-title m-0">${course.title}</h5>
                                        <span class="course-price"> LKR ${course.price.toFixed(2) || '0.00'}</span>
                                    </div>
                                    <p class="card-text text-muted">${course.description}</p>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <small class="text-muted">Duration: ${course.duration}</small>
                                        <button class="btn btn-light btn-sm enroll-btn" data-course-id="${course.id}">
                                            Enroll Now
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `;
                    coursesContainer.append(courseCard);
                });

                $('.enroll-btn').on('click', function() {
                    const courseId = $(this).data('course-id');
                    showCourseDetails(courseId);
                });
            },
            error: function(xhr, status, error) {
                console.error('Error loading courses:', error);
                $('#courses-container').html(`
                    <div class="col-12">
                        <div class="alert alert-danger">
                            Unable to load courses. Please try again later.
                        </div>
                    </div>
                `);
            }
        });
    }

    // Function to check enrollment status
    function checkEnrollment(courseId) {
        return $.ajax({
            url: `${BASE_URL}/api/v1/enrollments/existsByStudentIdAndCourseId`,
            method: 'POST',
            data: {
                userId: userId,
                courseId: courseId
            },
            headers: {
                'Authorization': 'Bearer ' + accessToken
            },
            dataType: 'json'
        });
    }

    // Modified showCourseDetails function
    function showCourseDetails(courseId) {
        $.ajax({
            url: `${BASE_URL}/api/v1/courses/course-by-id/${courseId}`,
            method: 'GET',
            timeout: 0,
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + accessToken
            },
            success: function(response) {
                const course = response.course;

                // Check enrollment status before showing payment button
                checkEnrollment(courseId)
                    .done(function(isEnrolled) {
                        const enrollButton = isEnrolled
                            ? `<a href="profile.html" class="btn btn-primary" style="background-color: #5fcf80;">Learning Now</a>`
                            : `<button type="button" class="btn btn-success" id="paymentBtn" data-course-id="${course.id}">
                                   Pay LKR ${course.price.toFixed(2) || '0.00'} and Enroll
                               </button>`;

                        const modalHtml = `
                            <div class="modal fade" id="courseDetailsModal" tabindex="-1" aria-labelledby="courseDetailsModalLabel" aria-hidden="true">
                                <div class="modal-dialog modal-lg">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="courseDetailsModalLabel">Course Details</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <img src="http://localhost:8080${course.imgUrl}" class="img-fluid rounded" alt="${course.title}">
                                                </div>
                                                <div class="col-md-6">
                                                    <h3>${course.title}</h3>
                                                    <p class="fs-4 text-success fw-bold">LKR ${course.price.toFixed(2) || '0.00'}</p>
                                                    <p><strong>Duration:</strong> ${course.duration}</p>
                                                    <p>${course.description}</p>
                                                    <hr>
                                                    <h5>What You'll Learn</h5>
                                                    <ul>
                                                        <li>Comprehensive curriculum designed by industry experts</li>
                                                        <li>Hands-on projects and real-world applications</li>
                                                        <li>24/7 access to course materials</li>
                                                        <li>Certificate upon completion</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                            ${enrollButton}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        `;

                        $('#courseDetailsModal').remove();
                        $('body').append(modalHtml);
                        const courseModal = new bootstrap.Modal(document.getElementById('courseDetailsModal'));
                        courseModal.show();

                        if (!isEnrolled) {
                            $('#paymentBtn').on('click', function() {
                                const courseId = $(this).data('course-id');
                                const price = $(this).data('course-price');
                                processPayment(courseId, price);
                            });
                        }
                    })
                    .fail(function(xhr, status, error) {
                        console.error('Error checking enrollment:', error);
                        alert('Unable to verify enrollment status. Please try again.');
                    });
            },
            error: function(xhr, status, error) {
                console.error('Error loading course details:', error);
                // alert('Unable to load course details. Please try again.');
                window.location.href = "../pages/login.html";
            }
        });
    }


    // Modified processPayment function for PayHere integration
    function processPayment(courseId) {
        $.ajax({
            url: `${BASE_URL}/api/v1/payments/initiate`,
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + accessToken
            },
            data: {
                userId: userId,
                courseId: courseId
            },
            success: function (response) {
                if (response.statusCode === 200) {
                    const payment = response.payment;

                    // Populate PayHere form fields
                    $('#merchant_id').val(payment.merchantId);
                    $('#order_id').val(payment.orderId);
                    $('#items').val('Course Enrollment'); // You can customize this
                    $('#currency').val(payment.currency);
                    $('#amount').val(payment.amount.toFixed(2));
                    $('#first_name').val(localStorage.getItem('firstName')); // Get user's first name
                    $('#last_name').val(localStorage.getItem('lastName'));   // Get user's last name
                    $('#email').val(localStorage.getItem('email'));         // Get user's email
                    $('#phone').val(localStorage.getItem('phone'));         // Get user's phone
                    // You might need to get address, city etc. from user profile if available
                    $('#address').val('Your Address');
                    $('#city').val('Your City');
                    $('#hash').val(payment.hash);

                    // Submit the form to PayHere
                    $('#payhere-form').submit();
                } else {
                    alert('Error initiating payment: ' + response.message);
                }
            },
            error: function (xhr, status, error) {
                console.error('Error initiating payment:', error);
                alert('Failed to initiate payment');
            }
        });
    }

    // // Function to initiate payment through the backend (unchanged)
    // function initiatePayment(courseId) {
    //     $.ajax({
    //         url: `${BASE_URL}/api/v1/payments/initiate`,
    //         method: 'POST',
    //         data: {
    //             userId: userId,
    //             courseId: courseId
    //         },
    //         headers: {
    //             'Authorization': 'Bearer ' + accessToken
    //         },
    //         success: function(response) {
    //             const successModalHtml = `
    //                 <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
    //                     <div class="modal-dialog">
    //                         <div class="modal-content">
    //                             <div class="modal-header bg-success text-white">
    //                                 <h5 class="modal-title" id="successModalLabel">Payment Successful</h5>
    //                                 <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
    //                             </div>
    //                             <div class="modal-body">
    //                                 <div class="text-center mb-4">
    //                                     <i class="bi bi-check-circle-fill text-success" style="font-size: 3rem;"></i>
    //                                 </div>
    //                                 <h4 class="text-center">Thank you for your purchase!</h4>
    //                                 <p class="text-center">Your course is now available in your profile.</p>
    //                             </div>
    //                             <div class="modal-footer">
    //                                 <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="closeSuccessBtn">Close</button>
    //                                 <a href="profile.html" class="btn btn-success">Go to My Courses</a>
    //                             </div>
    //                         </div>
    //                     </div>
    //                 </div>
    //             `;
    //
    //             $('#successModal').remove();
    //             $('body').append(successModalHtml);
    //             const successModal = new bootstrap.Modal(document.getElementById('successModal'));
    //             successModal.show();
    //
    //             $('#closeSuccessBtn').on('click', function() {
    //                 window.location.href = 'profile.html';
    //             });
    //         },
    //         error: function(xhr, status, error) {
    //             console.error('Payment initiation error:', error);
    //             const errorModalHtml = `
    //                 <div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
    //                     <div class="modal-dialog">
    //                         <div class="modal-content">
    //                             <div class="modal-header bg-danger text-white">
    //                                 <h5 class="modal-title" id="errorModalLabel">Payment Failed</h5>
    //                                 <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
    //                             </div>
    //                             <div class="modal-body">
    //                                 <p>Failed to process your payment. Please try again later.</p>
    //                             </div>
    //                             <div class="modal-footer">
    //                                 <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
    //                             </div>
    //                         </div>
    //                     </div>
    //                 </div>
    //             `;
    //
    //             $('#errorModal').remove();
    //             $('body').append(errorModalHtml);
    //             const errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
    //             errorModal.show();
    //         }
    //     });
    // }

    // Initial load of courses
    loadCourses();
});