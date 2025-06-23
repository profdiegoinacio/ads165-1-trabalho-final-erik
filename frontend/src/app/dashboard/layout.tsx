import SidebarEsquerda from "@/components/layout/SidebarEsquerda";
import SidebarDireita from "@/components/layout/SidebarDireita";

export default function DashboardLayout({
                                            children,
                                        }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div className="bg-black min-h-screen w-full flex justify-center">

            <aside className="w-64 border-r border-gray-800 h-screen sticky top-0 hidden md:block">
                <SidebarEsquerda />
            </aside>
            <main className="w-full md:w-[600px] border-r border-gray-800">
                {children}
            </main>

            <aside className="h-screen sticky top-0 hidden lg:block">
                <SidebarDireita />
            </aside>

        </div>
    );
}
