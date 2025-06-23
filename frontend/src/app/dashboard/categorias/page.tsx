import FiltroCategorias from "@/components/FiltroCategorias";
import { AreaDeAtuacao } from "@/types";
async function getAreasDeAtuacao(): Promise<AreaDeAtuacao[]> {
    try {
        const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/areas-de-atuacao`, { cache: 'no-store' });
        if (!response.ok) return [];
        return await response.json();
    } catch (error) {
        console.error("Falha ao buscar dados iniciais de categorias:", error);
        return [];
    }
}

export default async function PaginaCategorias() {

    const areas = await getAreasDeAtuacao();

    return (
        <div className="p-4 sm:p-6 md:p-8 text-white">
            <h1 className="text-3xl font-bold mb-6">Encontre Profissionais por Categoria</h1>
            <FiltroCategorias areasIniciais={areas} />
        </div>
    );
}