<script>
	import './layout.css';
	import favicon from '$lib/assets/favicon.svg';
	import { authStore } from '$lib/stores/authStore.js';
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import { browser } from '$app/environment';

	let { children } = $props();

	const PUBLIC_ROUTES = ['/login', '/register'];

	$effect(() => {
		if (browser && !$authStore.email && !PUBLIC_ROUTES.some((r) => $page.url.pathname.startsWith(r))) {
			goto('/login');
		}
	});

	function logout() {
		authStore.logout();
		goto('/login');
	}
</script>

<svelte:head><link rel="icon" href={favicon} /></svelte:head>

{#if $authStore.email}
	<nav class="flex items-center justify-between border-b bg-white px-6 py-3">
		<a href="/dashboard" class="text-lg font-bold text-gray-800">Shop</a>
		<div class="flex items-center gap-4">
			<span class="text-sm text-gray-600">{$authStore.email}</span>
			<button onclick={logout} class="text-sm text-red-600 hover:underline">Logout</button>
		</div>
	</nav>
{/if}

{@render children()}
