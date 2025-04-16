$(document).ready(function() {
    // Show Forgot Password Modal
    $('#forgotPasswordLink').on('click', function(e) {
        e.preventDefault();
        $('#forgotPasswordModal').show();
        resetForms();
    });

    // Close Modal
    $('#closeForgotModal').on('click', function() {
        $('#forgotPasswordModal').hide();
        resetForms();
    });

    // Reset forms to initial state
    function resetForms() {
        $('#forgotPasswordForm')[0].reset();
        $('#verifyOtpForm')[0].reset();
        $('#resetPasswordForm')[0].reset();
        $('#forgotPasswordForm').show();
        $('#verifyOtpForm').hide();
        $('#resetPasswordForm').hide();
        $('#otpFeedback').hide(); // Hide feedback area
        $('.alert').hide();
    }

    // Step 1: Request OTP
    $('#forgotPasswordForm').on('submit', function(e) {
        e.preventDefault();
        $('.alert').hide();

        const email = $('#forgotEmail').val().trim();

        // Validate email
        if (!validateEmail(email)) {
            $('#otpFeedback').text('Please enter a valid email address.').show().addClass('alert-danger');
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/forgot-password',
            type: 'POST',
            data: { email: email },
            success: function(response) {
                $('#otpFeedback').text('OTP sent successfully! Please check your email.').show().removeClass('alert-danger').addClass('alert-success');
                $('#forgotPasswordForm').hide();
                $('#verifyOtpForm').show();
            },
            error: function(xhr) {
                let errorMessage = 'Failed to send OTP. Please try again.';
                try {
                    errorMessage = xhr.responseText || errorMessage;
                } catch(e) {}
                $('#otpFeedback').text(errorMessage).show().removeClass('alert-success').addClass('alert-danger');
            }
        });
    });

    // Step 2: Verify OTP
    $('#verifyOtpForm').on('submit', function(e) {
        e.preventDefault();
        $('.alert').hide();

        const email = $('#forgotEmail').val().trim();
        const otp = $('#otp').val().trim();

        // Validate OTP
        if (!otp) {
            $('#otpFeedback').text('Please enter the OTP.').show().addClass('alert-danger');
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/verify-otp',
            type: 'POST',
            data: { email: email, otp: otp },
            success: function(response) {
                $('#otpFeedback').text('OTP verified successfully!').show().removeClass('alert-danger').addClass('alert-success');
                $('#verifyOtpForm').hide();
                $('#resetPasswordForm').show();
            },
            error: function(xhr) {
                let errorMessage = 'Invalid or expired OTP.';
                try {
                    errorMessage = xhr.responseText || errorMessage;
                } catch(e) {}
                $('#otpFeedback').text(errorMessage).show().removeClass('alert-success').addClass('alert-danger');
            }
        });
    });

    // Step 3: Reset Password
    $('#resetPasswordForm').on('submit', function(e) {
        e.preventDefault();
        $('.alert').hide();

        const email = $('#forgotEmail').val().trim();
        const otp = $('#otp').val().trim();
        const newPassword = $('#newPassword').val().trim();
        const confirmPassword = $('#confirmPassword').val().trim();

        // Validate passwords
        if (!newPassword || !confirmPassword) {
            $('#otpFeedback').text('Please fill in both password fields.').show().addClass('alert-danger');
            return;
        }

        // Password validation regex
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

        if (!passwordRegex.test(newPassword)) {
            $('#otpFeedback').text('Password must be at least 8 characters long, with at least one uppercase letter, one lowercase letter, one number, and one special character.').show().addClass('alert-danger');
            return;
        }

        if (newPassword !== confirmPassword) {
            $('#otpFeedback').text('Passwords do not match.').show().addClass('alert-danger');
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/reset-password',
            type: 'POST',
            data: { email: email, otp: otp, newPassword: newPassword },
            success: function(response) {
                $('#otpFeedback').text('Password reset successfully! You can now log in.').show().removeClass('alert-danger').addClass('alert-success');
                setTimeout(function() {
                    $('#forgotPasswordModal').hide();
                    resetForms();
                }, 1500);
            },
            error: function(xhr) {
                let errorMessage = 'Failed to reset password. Please try again.';
                try {
                    errorMessage = xhr.responseText || errorMessage;
                } catch(e) {}
                $('#otpFeedback').text(errorMessage).show().removeClass('alert-success').addClass('alert-danger');
            }
        });
    });

    // Login Form Submission
    $('#loginForm').on('submit', function(e) {
        e.preventDefault();
        $('#alertSuccess').hide();
        $('#alertError').hide();

        const loginData = {
            email: $('#email').val().trim(),
            password: $('#password').val().trim()
        };

        // Validate login fields
        if (!validateEmail(loginData.email) || !loginData.password) {
            $('#alertError').text('Please enter valid email and password.').show();
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(loginData),
            success: function(response) {
                if (response.statusCode === 200) {
                    $('#alertSuccess').text('Login successful! Redirecting...').show();
                    if (response.token) {
                        localStorage.setItem('userToken', response.token);
                    }
                    if (response.role) {
                        localStorage.setItem('userRole', response.role);
                    }
                    if (response.expirationTime) {
                        localStorage.setItem('tokenExpiration', response.expirationTime);
                    }
                    setTimeout(function() {
                        window.location.href = 'index.html';
                    }, 1500);
                } else {
                    $('#alertError').text(response.message || 'Login failed. Please check your credentials.').show();
                }
            },
            error: function(xhr) {
                let errorMessage = 'Login failed. Please check your credentials.';
                try {
                    const response = JSON.parse(xhr.responseText);
                    errorMessage = response.message || errorMessage;
                } catch(e) {}
                $('#alertError').text(errorMessage).show();
            }
        });
    });

    // Email validation function
    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    }
});