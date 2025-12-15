<template>
    <header class="sticky top-0 z-50 transition-all duration-300">
        <section :class="[
            'w-full py-4 transition-all duration-300',
            isScrolled ? 'bg-gray-100/95 backdrop-blur-md shadow-md' : 'bg-gray-100'
        ]">

            <div class="flex flex-col md:flex-row items-center justify-between gap-3 md:gap-6 px-4 md:px-6">

                <!-- Logo + Mobile menu -->
                <div class="flex justify-between items-center w-full md:w-auto">
                    <a href="/" class="flex items-center hover:scale-105 transition-transform duration-300">
                        <span class="text-3xl md:text-5xl font-extrabold text-black">High</span>
                        <span class="text-3xl md:text-5xl font-extrabold text-red-600">Sport</span>
                    </a>

                    <button class="md:hidden text-gray-700 hover:text-indigo-600" aria-label="Toggle mobile menu"
                        @click="isMobileMenuOpen = !isMobileMenuOpen">
                        <Icon icon="mdi:menu" class="w-6 h-6" />
                    </button>
                </div>

                <!-- Search bar -->
                <form class="w-full md:flex-1 max-w-xl" role="search">
                    <label class="relative w-full">
                        <input type="text" placeholder="Tìm kiếm...."
                            class="w-full px-4 py-3 text-sm md:text-base border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500">
                        <button type="submit"
                            class="absolute right-2 top-1/2 -translate-y-1/2 text-gray-500 hover:text-indigo-600">
                            <Icon icon="mdi:magnify" class="w-4 h-4" />
                        </button>
                    </label>
                </form>

                <!-- Icons -->
                <aside class="flex items-center justify-end space-x-4 w-full md:w-auto mt-2 md:mt-0">
                    <button class="relative p-2 text-gray-700 hover:text-pink-600" aria-label="Wishlist">
                        <Icon icon="mdi:heart-outline" class="w-6 h-6" />
                        <span
                            class="absolute -top-1 -right-1 bg-pink-600 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                            3
                        </span>
                    </button>

                    <button class="relative p-2 text-gray-700 hover:text-pink-600" aria-label="Cart">
                        <Icon icon="mdi:cart-outline" class="w-6 h-6" />
                        <span
                            class="absolute -top-1 -right-1 bg-pink-600 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                            5
                        </span>
                    </button>

                    <button class="relative p-2 text-gray-700 hover:text-pink-600" aria-label="Account">
                        <Icon icon="mdi:account-outline" class="w-6 h-6" />
                    </button>
                </aside>
            </div>
        </section>

        <!-- Navbar -->
        <nav class="bg-pink-950">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <ul class="hidden md:flex justify-center py-3 flex-wrap gap-x-12 text-xl font-medium text-white">
                    <li v-for="item in navItems" :key="item.id">
                        <a :href="item.link" class="hover:text-pink-300 transition-colors">
                            {{ item.name }}
                        </a>
                    </li>
                </ul>

                <!-- Mobile menu -->
                <section v-if="isMobileMenuOpen"
                    class="md:hidden mt-2 bg-white rounded-lg shadow-md p-4 space-y-3 text-[#5d4037] text-center">
                    <a v-for="item in navItems" :href="item.link" :key="item.id"
                        class="block hover:text-amber-600 text-sm font-medium">
                        {{ item.name }}
                    </a>
                </section>
            </div>
        </nav>
    </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { Icon } from '@iconify/vue'

const isScrolled = ref(false)
const isMobileMenuOpen = ref(false)

const navItems = [
    { id: 1, name: 'Trang Chủ', link: '/' },
    { id: 2, name: 'Nam', link: '#products' },
    { id: 3, name: 'Nữ', link: '#about' },
    { id: 4, name: 'Trẻ em', link: '#contact' },
]

const handleScroll = () => {
    isScrolled.value = window.scrollY > 10
}

onMounted(() => window.addEventListener('scroll', handleScroll))
onUnmounted(() => window.removeEventListener('scroll', handleScroll))
</script>
