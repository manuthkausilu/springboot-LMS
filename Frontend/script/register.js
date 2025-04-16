$(document).ready(function() {
    $('#registrationForm').on('submit', function(e) {
        e.preventDefault();

        // Clear previous alerts
        $('#alertSuccess').hide();
        $('#alertError').hide();

        // Check if passwords match
        const password = $('#password').val();
        const confirmPassword = $('#confirmPassword').val();

        if (password !== confirmPassword) {
            $('#alertError').text('Passwords do not match').show();
            return;
        }

        // Prepare data for API
        const userData = {
            name: $('#name').val(),
            email: $('#email').val(),
            phoneNumber: $('#phone').val(),
            password: password
            // Add other fields as needed according to your User entity
        };

        // Make AJAX call to register endpoint
        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/register',  // Adjust the URL based on your server configuration
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function(response) {
                if (response.statusCode === 200 || response.statusCode === 201) {
                    $('#alertSuccess').text(response.message || 'Registration successful!').show();
                    $('#registrationForm')[0].reset();

                    // Redirect to login page after a delay
                    setTimeout(function() {
                        window.location.href = 'login.html';
                    }, 2000);
                } else {
                    $('#alertError').text(response.message || 'Registration failed. Please try again.').show();
                }
            },
            error: function(xhr, status, error) {
                let errorMessage = 'Registration failed. Please try again.';

                try {
                    const response = JSON.parse(xhr.responseText);
                    errorMessage = response.message || errorMessage;
                } catch(e) {
                    console.error('Error parsing response:', e);
                }

                $('#alertError').text(errorMessage).show();
            }
        });
    });
});