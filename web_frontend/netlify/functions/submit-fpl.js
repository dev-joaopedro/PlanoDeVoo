const { Client } = require('pg');

exports.handler = async (event, context) => {
    // Apenas aceita requisições POST
    if (event.httpMethod !== "POST") {
        return { statusCode: 405, body: "Method Not Allowed" };
    }

    const data = JSON.parse(event.body);

    // Conexão com o banco usando variável de ambiente configurada no Netlify
    const client = new Client({
        connectionString: process.env.DATABASE_URL,
        ssl: { rejectUnauthorized: false } // Necessário para Neon
    });

    try {
        await client.connect();

        const query = `
            INSERT INTO plano_voo (
                identificacao_aeronave, regras, tipo_voo, 
                numero_aeronave, tipo_aeronave, catet, 
                equipamento, equipamento_vigilancia, 
                aerodromo_pt, hora_eob, velocidade, 
                nivel_voo, rota, aerodromo_destino, 
                duracao_total_voo, aerodromo_alternativa, observacoes
            ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17)
        `;

        const values = [
            data.identaeronave, data.regras, data.tipodevoo,
            data.numaeronave, data.tipoaeronave, data.catet,
            data.equipamento, data.equipamentov,
            data.aerodromopt, data.horaeobt, data.velocidade,
            data.niveldevoo, data.rota, data.aerodromodestino,
            data.duracaototalvoo, data.aerodromoalternativa, data.observacoes
        ];

        await client.query(query, values);

        return {
            statusCode: 200,
            body: JSON.stringify({ message: "Plano de voo salvo no Neon com sucesso!" })
        };
    } catch (err) {
        console.error('Database error:', err);
        return {
            statusCode: 500,
            body: JSON.stringify({ error: "Erro ao salvar no banco de dados: " + err.message })
        };
    } finally {
        await client.end();
    }
};
