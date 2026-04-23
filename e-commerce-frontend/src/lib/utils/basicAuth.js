export function encodeBasicAuth(email, password) {
	return btoa(`${email}:${password}`);
}

export function getAuthHeader(email, password) {
	return `Basic ${encodeBasicAuth(email, password)}`;
}
