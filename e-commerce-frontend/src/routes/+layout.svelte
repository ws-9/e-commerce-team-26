<script>
	import './layout.css';
	import { resolve } from '$app/paths';
	import favicon from '$lib/assets/favicon.svg';
	import { authStore } from '$lib/stores/authStore.js';
	import { cartStore } from '$lib/stores/cartStore.js';
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import { browser } from '$app/environment';

	let { children } = $props();

	const PUBLIC_ROUTES = ['/login', '/register'];

	$effect(() => {
		if (
			browser &&
			!$authStore.email &&
			!PUBLIC_ROUTES.some((r) => $page.url.pathname.startsWith(r))
		) {
			goto(resolve('/login'));
		}
	});

	function logout() {
		authStore.logout();
		goto(resolve('/login'));
	}
</script>

<svelte:head><link rel="icon" href={favicon} /></svelte:head>

{#if $authStore.email && !PUBLIC_ROUTES.some((r) => $page.url.pathname.startsWith(r))}
	<header class="sticky top-0 z-50 border-b border-slate-200 bg-white/80 backdrop-blur-md">
		<nav class="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
			<a href={resolve('/products')} class="text-xl font-black tracking-tight text-slate-950">
				E-Commerce Team 26
			</a>

			<div class="flex items-center gap-6">
				<div class="hidden items-center gap-4 sm:flex">
					<a
						href={resolve('/orders')}
						class="text-sm font-bold text-slate-600 transition hover:text-slate-950"
					>
						My Orders
					</a>
					<div class="h-4 w-px bg-slate-200"></div>
					<div class="flex items-center gap-2">
						<a
							href={resolve('/profile')}
							class="text-sm font-medium text-slate-600 transition hover:text-slate-950"
						>
							{$authStore.name ?? $authStore.email}
						</a>
						<button
							onclick={logout}
							class="text-sm font-bold text-red-600 transition hover:text-red-700"
						>
							Logout
						</button>
					</div>
				</div>

				<div class="h-6 w-px bg-slate-200"></div>

				<a
					href={resolve('/cart')}
					class="relative flex items-center gap-2 rounded-xl bg-slate-950 px-4 py-2 text-sm font-bold text-white transition hover:bg-slate-800"
				>
					Cart
					{#if $cartStore.length > 0}
						<span class="flex h-5 w-5 items-center justify-center rounded-full bg-blue-500 text-[10px]">
							{$cartStore.length}
						</span>
					{/if}
				</a>
			</div>
		</nav>
	</header>
{/if}

{@render children()}
