<script>
	import { authStore } from '$lib/stores/authStore.js';
	import { goto } from '$app/navigation';

	function logout() {
		authStore.logout();
		goto('/login');
	}
</script>

<div class="p-8">
	<h1 class="mb-2 text-2xl font-bold text-gray-900">Seller Dashboard</h1>
	<p class="mb-1 text-gray-500">Welcome, {$authStore.email}</p>
	{#if $authStore.businessName}
		<p class="mb-6 text-sm text-gray-400">{$authStore.businessName}</p>
	{/if}
	<button onclick={logout} class="rounded-lg bg-red-600 px-4 py-2 text-sm text-white hover:bg-red-700">
		Logout
	</button>
</div>
