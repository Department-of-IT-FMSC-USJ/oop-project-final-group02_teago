const BASE_URL = "http://localhost:8080/api";

async function apiCall(endpoint, method = "GET", body = null) {
    const token = localStorage.getItem("token");

    const options = {
        method: method,
        headers: {
            "Content-Type": "application/json",
            ...(token && { "Authorization": "Bearer " + token })
        }
    };

    if (body) options.body = JSON.stringify(body);

    const response = await fetch(BASE_URL + endpoint, options);

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Something went wrong");
    }

    return response.json();
}