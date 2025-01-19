document.addEventListener("DOMContentLoaded", () => {
    const tableBody = document.querySelector('#checkoutTable tbody');
    const totalPriceElement = document.getElementById('totalPrice');
    const checkoutForm = document.getElementById('checkout-form');
    const pickupDateInput = document.getElementById('pickupDate');
    const pickupTimeInput = document.getElementById('pickupTime');
    const paymentMethodSelect = document.getElementById('paymentMethod');
    const qrCodeContainer = document.getElementById('qrCodeContainer');
    const qrCodeImage = document.getElementById('qrCodeImage');


    // Set minimum date for pickupDate and disable weekends
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const minDate = `${year}-${month}-${day}`;
    pickupDateInput.setAttribute('min', minDate);


    pickupDateInput.addEventListener('input', () => {
        const selectedDate = new Date(pickupDateInput.value);
        const dayOfWeek = selectedDate.getDay();
        if (dayOfWeek === 6 || dayOfWeek === 0) { // Saturday = 6, Sunday = 0
            alert("The bakery is closed on Saturdays and Sundays. Please select a different date.");
            pickupDateInput.value = ""; // Clear the invalid date
        }
    });


    // Load cart data from localStorage
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    let totalPrice = 0;


    // Populate cart items in the table
    if (cart.length === 0) {
        tableBody.innerHTML = "<tr><td colspan='3'>Your cart is empty.</td></tr>";
        totalPriceElement.textContent = "RM 0.00";
    } else {
        cart.forEach((item) => {
            const price = parseFloat(item.price) || 0;
            const quantity = parseInt(item.quantity) || 1;


            const row = document.createElement('tr');
            row.innerHTML = `
               <td>${item.name || 'Undefined'}</td>
               <td>RM ${price.toFixed(2)}</td>
               <td>${quantity}</td>
           `;
            tableBody.appendChild(row);


            totalPrice += price * quantity;
        });


        totalPriceElement.textContent = `RM ${totalPrice.toFixed(2)}`;
    }


    // Real-time validation for pickup time
    pickupTimeInput.addEventListener('input', () => {
        const selectedTime = pickupTimeInput.value;
        if (!isTimeWithinRange(selectedTime)) {
            pickupTimeInput.setCustomValidity(
                "Please select a time between 10:00 AM and 10:00 PM."
            );
        } else {
            pickupTimeInput.setCustomValidity(""); // Clear the error message
        }
    });


    // Function to check if time is within range (10:00 AM to 10:00 PM)
    function isTimeWithinRange(time) {
        if (!time) return false; // If no time is selected
        const [hours, minutes] = time.split(':').map(Number);
        const totalMinutes = hours * 60 + minutes;
        const startMinutes = 10 * 60; // 10:00 AM in minutes
        const endMinutes = 22 * 60; // 10:00 PM in minutes
        return totalMinutes >= startMinutes && totalMinutes <= endMinutes;
    }


    // Handle payment method selection to display QR code image
    paymentMethodSelect.addEventListener('change', () => {
        const selectedMethod = paymentMethodSelect.value;


        if (selectedMethod === "duitnow") {
            qrCodeImage.src = "img/duitnow-qr.png"; // Replace with the actual path to your DuitNow QR image
            qrCodeContainer.style.display = "block";
        } else if (selectedMethod === "tng") {
            qrCodeImage.src = "img/tng-qr.jpeg"; // Replace with the actual path to your TNG QR image
            qrCodeContainer.style.display = "block";
        } else {
            qrCodeContainer.style.display = "none";
        }
    });


    // Handle form submission
    checkoutForm.addEventListener('submit', function (event) {
        event.preventDefault();


        const fullName = document.getElementById('fullName').value;
        const phoneNumber = document.getElementById('phoneNumber').value;
        const email = document.getElementById('email').value;
        const pickupDate = pickupDateInput.value;
        const pickupTime = pickupTimeInput.value;
        const address = document.getElementById('address')?.value || "123 Bakery Lane, Sweetville";


        // Validate all required fields
        if (!fullName || !phoneNumber || !email || !pickupDate || !pickupTime || !address) {
            alert('Please fill all required fields!');
            return;
        }


        // Validate pickup time before submission
        if (!isTimeWithinRange(pickupTime)) {
            alert("Pickup time must be between 10:00 AM and 10:00 PM.");
            return;
        }


        // Save order details
        const orderDetails = {
            fullName,
            phoneNumber,
            email,
            pickupDate,
            pickupTime,
            address,
            items: cart,
            totalPrice: totalPrice.toFixed(2),
        };


        localStorage.setItem('orderDetails', JSON.stringify(orderDetails));
        localStorage.removeItem('cart');
        alert("Order successfully placed!");
        window.location.href = 'receipt.html'; // Redirect to receipt page
    });
});
