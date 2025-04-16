$(document).ready(function() {
    function updateCounts() {
        // Fetch user count
        $.ajax({
            url: "http://localhost:8080/api/v1/users/userCount",
            type: "GET",
            dataType: "json",
            success: function(userCount) {
                console.log("AJAX Success - User Count:", userCount);

                var $studentSpan = $("#counts")
                    .find("p:contains('Students')")
                    .siblings(".purecounter");

                console.log("Element selected for Students:", $studentSpan);

                $studentSpan.attr('data-purecounter-end', userCount); // Update the data attribute

                // Re-initialize PureCounter
                new PureCounter();
            },
            error: function(xhr, status, error) {
                console.error("Error fetching user count:", error);
            }
        });

        // Fetch course count
        $.ajax({
            url: "http://localhost:8080/api/v1/courses/courseCount",
            type: "GET",
            dataType: "json",
            success: function(courseCount) {
                console.log("AJAX Success - Course Count:", courseCount);

                var $courseSpan = $("#counts")
                    .find("p:contains('Courses')")
                    .siblings(".purecounter");

                console.log("Element selected for Courses:", $courseSpan);

                $courseSpan.attr('data-purecounter-end', courseCount); // Update the data attribute

                // Re-initialize PureCounter
                new PureCounter();
            },
            error: function(xhr, status, error) {
                console.error("Error fetching course count:", error);
            }
        });
    }

    updateCounts();
});