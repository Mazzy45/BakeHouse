// Function to handle adding products to cart
function addToCart(productId) {
    alert(`Product with ID ${productId} added to cart.`);
    // Simulate server-side logic; update a cart object in a real scenario.
}

// Handle form validation for login or register
function validateForm(event) {
    const form = event.target;
    const username = form.querySelector('input[name="username"]').value;
    const password = form.querySelector('input[name="password"]').value;
    if (!username || !password) {
        alert("Please fill out all fields.");
        event.preventDefault();
    }
}

// Attach form validation to login/register forms
document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");
    if (loginForm) loginForm.addEventListener("submit", validateForm);
    if (registerForm) registerForm.addEventListener("submit", validateForm);
});
