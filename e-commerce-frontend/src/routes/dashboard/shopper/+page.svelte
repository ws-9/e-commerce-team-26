<script>
	import { goto } from '$app/navigation';
	import { get } from 'svelte/store';
	import { auth } from '$lib/stores/auth.js';

	const state = get(auth);
	const user = state.user;

	function logout() {
		auth.clearAuth();
		goto('/login');
	}
</script>

<div class="min-h-screen bg-gray-50">
	<header class="bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
		<h1 class="text-lg font-semibold text-gray-800">Shopper Dashboard</h1>
		<div class="flex items-center gap-4">
			<span class="text-sm text-gray-600">Welcome, {user?.name ?? 'Shopper'}</span>
			<button
				onclick={logout}
				class="text-sm text-red-600 hover:underline font-medium"
			>
				Logout
			</button>
		</div>
	</header>

	<main class="max-w-4xl mx-auto px-6 py-10">
		<div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 text-center">
			<p class="text-4xl mb-4">🛍️</p>
			<h2 class="text-xl font-semibold text-gray-800 mb-2">Browse & Shop</h2>
			<p class="text-gray-500 text-sm">Product listings and shopping features will appear here.</p>
		</div>
	</main>
</div>
