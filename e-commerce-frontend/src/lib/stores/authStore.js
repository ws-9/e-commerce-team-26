import { writable } from 'svelte/store';
import { browser } from '$app/environment';
import { deriveRole } from '$lib/services/storefrontMappers.js';
import { getAuthHeader } from '$lib/utils/basicAuth.js';

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
	const authHeader = getAuthHeader(email, password);
	const res = await fetch('http://localhost:8080/api/me', {
		headers: { Authorization: authHeader }
	});
	if (!res.ok) {
		throw new Error(
			res.status === 401 || res.status === 400
				? 'Invalid email or password'
				: 'Authentication failed'
		);
	}
	const data = await res.json();
	let sellerInfo = null;

	if (data.isSeller) {
		try {
			const sellerRes = await fetch(`http://localhost:8080/api/sellers/${data.id}`, {
				headers: { Authorization: authHeader }
			});
			if (sellerRes.ok) {
				sellerInfo = await sellerRes.json();
			}
		} catch (err) {
			console.error('Failed to fetch seller info', err);
		}
	}

	return {
		email,
		password,
		userRole: deriveRole(data),
		userID: data.id,
		name: data.name,
		address: data.address,
		seller: sellerInfo
	};
}

const empty = { email: null, password: null, userRole: null, userID: null, seller: null };
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
		const res = await fetch('http://localhost:8080/api/auth/register', {
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
		const res = await fetch('http://localhost:8080/api/auth/register', {
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
	},

	async updateProfile(name, address) {
		let current;
		store.subscribe((v) => (current = v))();

		const res = await fetch(`http://localhost:8080/api/users/${current.userID}`, {
			method: 'PATCH',
			headers: {
				'Content-Type': 'application/json',
				Authorization: getAuthHeader(current.email, current.password)
			},
			body: JSON.stringify({ name, address })
		});

		if (!res.ok) throw new Error('Update failed');

		const updated = { ...current, name, address };
		store.set(updated);
		persistAuth(updated);
		return updated;
	},

	async deleteAccount() {
		let current;
		store.subscribe((v) => (current = v))();

		const res = await fetch('http://localhost:8080/api/users', {
			method: 'DELETE',
			headers: {
				Authorization: getAuthHeader(current.email, current.password)
			}
		});

		if (!res.ok) throw new Error('Deletion failed');

		store.set(empty);
		clearAuth();
	},

	async becomeSeller(sellerData) {
		let current;
		store.subscribe((v) => (current = v))();

		const res = await fetch('http://localhost:8080/api/sellers', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				Authorization: getAuthHeader(current.email, current.password)
			},
			body: JSON.stringify(sellerData)
		});

		if (!res.ok) throw new Error('Failed to become a seller');

		// Re-fetch user info to get the new role and seller details
		const auth = await callMe(current.email, current.password);
		store.set(auth);
		persistAuth(auth);
		return auth;
	},

	async updateSeller(sellerData) {
		let current;
		store.subscribe((v) => (current = v))();

		const res = await fetch('http://localhost:8080/api/sellers', {
			method: 'PATCH',
			headers: {
				'Content-Type': 'application/json',
				Authorization: getAuthHeader(current.email, current.password)
			},
			body: JSON.stringify(sellerData)
		});

		if (!res.ok) throw new Error('Failed to update seller profile');

		// Re-fetch user info to get updated seller details
		const auth = await callMe(current.email, current.password);
		store.set(auth);
		persistAuth(auth);
		return auth;
	},

	async deleteSellerProfile() {
		let current;
		store.subscribe((v) => (current = v))();

		const res = await fetch('http://localhost:8080/api/sellers', {
			method: 'DELETE',
			headers: {
				Authorization: getAuthHeader(current.email, current.password)
			}
		});

		if (!res.ok) throw new Error('Failed to delete business profile');

		// Re-fetch user info to update role back to SHOPPER
		const auth = await callMe(current.email, current.password);
		store.set(auth);
		persistAuth(auth);
		return auth;
	}
};
