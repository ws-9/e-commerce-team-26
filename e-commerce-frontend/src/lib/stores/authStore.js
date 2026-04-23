import { writable } from 'svelte/store';
import { browser } from '$app/environment';

function getStoredAuth() {
	if (!browser) return null;
	try {
		const raw = localStorage.getItem('auth');
		return raw ? JSON.parse(raw) : null;
	} catch {
		return null;
	}
}

function persistAuth(data) {
	if (browser) localStorage.setItem('auth', JSON.stringify(data));
}

function clearAuth() {
	if (browser) localStorage.removeItem('auth');
}

async function callMe(email, password) {
	const res = await fetch('/me', {
		headers: { Authorization: `Basic ${btoa(`${email}:${password}`)}` }
	});
	if (!res.ok) {
		throw new Error(
			res.status === 401 || res.status === 400 ? 'Invalid email or password' : 'Authentication failed'
		);
	}
	const data = await res.json();
	return { email, password, userRole: data.role, userID: data.id };
}

const empty = { email: null, password: null, userRole: null, userID: null };
const store = writable(getStoredAuth() ?? empty);

export const authStore = {
	subscribe: store.subscribe,

	// Validates credentials against /me and stores the result
	async validateCredentials(email, password) {
		const auth = await callMe(email, password);
		store.set(auth);
		persistAuth(auth);
		return auth;
	},

	// Login: call GET /me with HTTP Basic auth to verify credentials and get userID
	async login(email, password) {
		const auth = await callMe(email, password);
		store.set(auth);
		persistAuth(auth);
		return auth;
	},

	logout() {
		store.set(empty);
		clearAuth();
	},

	async registerShopper(email, password, name, address) {
		const res = await fetch('/api/auth/register', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ email, password, name, address, role: 'SHOPPER' })
		});
		if (!res.ok) throw new Error('Registration failed');
		const auth = await callMe(email, password);
		store.set(auth);
		persistAuth(auth);
		return auth;
	},

	async registerSeller(email, password, name, address, sellerExtras) {
		const res = await fetch('/api/auth/register', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ email, password, name, address, role: 'SELLER' })
		});
		if (!res.ok) throw new Error('Registration failed');
		const auth = await callMe(email, password);
		// Seller extras (businessName, taxID, contactEmail, description) are stored
		// in localStorage only — they are NOT sent to the backend
		const withExtras = { ...auth, ...sellerExtras };
		store.set(withExtras);
		persistAuth(withExtras);
		return withExtras;
	}
};
