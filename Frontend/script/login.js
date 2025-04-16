$(document).ready(function() {
    $('#loginForm').on('submit', function(e) {
        e.preventDefault();

        // Clear previous alerts
        $('#alertSuccess').hide();
        $('#alertError').hide();

        // Prepare login data
        const loginData = {
            email: $('#email').val(),
            password: $('#password').val()
        };

        // Make AJAX call to login endpoint
        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/login',  // Adjust the URL based on your server configuration
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(loginData),
            success: function(response) {
                console.log(response); // Log the entire response

                if (response.statusCode === 200) {
                    $('#alertSuccess').text('Login successful! Redirecting...').show();

                    // Store token in localStorage
                    if (response.token) {
                        localStorage.setItem('userToken', response.token);
                    }

                    // Optionally store user role and expiration time
                    if (response.role) {
                        localStorage.setItem('userRole', response.role);
                    }

                    if (response.expirationTime) {
                        localStorage.setItem('tokenExpiration', response.expirationTime);
                    }

                    // Redirect to index page after a brief delay
                    setTimeout(function() {
                        window.location.href = 'index.html';
                    }, 1500);
                } else {
                    $('#alertError').text(response.message || 'Login failed. Please check your credentials.').show();
                }
            },
            error: function(xhr, status, error) {
                let errorMessage = 'Login failed. Please check your credentials.';

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