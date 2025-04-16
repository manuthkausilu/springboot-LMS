$(document).ready(function() {
    // Retrieve the Bearer token from local storage
    var accessToken = localStorage.getItem('userToken');

    // Check if the token exists
    if (accessToken) {
        // Make AJAX call to get logged-in user profile info
        $.ajax({
            url: 'http://localhost:8080/api/v1/users/get-logged-in-profile-info', // Adjust the URL as necessary
            method: 'GET',
            timeout: 0,
            headers: {
                'Authorization': 'Bearer ' + accessToken // Include the Bearer token
            },
            success: function(response) {
                if (response.user.role === "ADMIN"){
                    $('#admin-nav').show();
                }
                if (response.statusCode === 200) {
                    // Process the user data
                    $('#sign-in-button').hide();
                    $('#profile-nav').show();
                    const user = response.user; // Access the user object from the response
                    $('#user-name').text(user.name || user.email); // Update the UI with user info
                    $('#user-profile').show(); // Show user profile
                } else {
                    console.error("Error fetching user profile:", response.message);
                }
            },
            error: function(xhr, status, error) {
                console.error("AJAX request failed:", status, error);
                // Handle unauthorized access (e.g., redirect to login)
                if (xhr.status === 401) {
                    window.location.href = 'login.html'; // Redirect to login page
                }
            }
        });
    } else {
        // No token found, redirect to login page
        window.location.href = '#'; // Redirect to login page
    }

    // Logout functionality
    $('#logout-button').on('click', function(e) {
        e.preventDefault();
        // Clear local storage
        localStorage.removeItem('userToken');
        localStorage.removeItem('userData');
        // Redirect to login page
        window.location.href = 'index.html';
    });
});