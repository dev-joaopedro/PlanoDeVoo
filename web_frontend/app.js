document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('fpl-form');
    const btnCancel = document.getElementById('btn-cancel');

    // Validação de entrada (Regex)
    const validate = (text, type) => {
        switch (type) {
            case 'letras': return /^[a-zA-Z]+$/.test(text);
            case 'numeros': return /^\d+$/.test(text);
            case 'horario': return /^\d{4}$/.test(text);
            default: return true;
        }
    };

    // Função para mostrar Toasts
    const showToast = (title, message, type = 'success') => {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        
        toast.innerHTML = `
            <div class="toast-content">
                <h4>${title}</h4>
                <p>${message}</p>
            </div>
        `;
        
        container.appendChild(toast);
        
        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateX(20px)';
            toast.style.transition = 'all 0.3s ease';
            setTimeout(() => toast.remove(), 300);
        }, 5000);
    };

    // Handler do formulário
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Coleta de dados
        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());
        
        // Regras (Checkbox multiple)
        const regras = Array.from(formData.getAll('regras'));
        data.regras = regras.join(', ');

        // Validações Manuais (baseadas no código Java)
        if (!data.regras) {
            showToast('Erro de Validação', 'Ao menos uma Regra deve ser selecionada!', 'error');
            return;
        }

        const numericFields = [
            { id: 'numaeronave', name: 'Número da Aeronave' },
            { id: 'duracaototalvoo', name: 'Duração do Voo' }
        ];

        for (const field of numericFields) {
            const val = document.getElementById(field.id).value.trim();
            if (!validate(val, 'numeros')) {
                showToast('Erro de Validação', `${field.name} deve conter apenas números.`, 'error');
                return;
            }
        }

        const hora = document.getElementById('horaeobt').value.trim();
        if (!validate(hora, 'horario')) {
            showToast('Erro de Validação', 'Hora EOBT deve estar no formato HHMM (ex: 1430).', 'error');
            return;
        }

        const letterFields = [
            { id: 'aerodromodestino', name: 'Aeródromo de Destino' },
            { id: 'aerodromoalternativa', name: 'Aeródromo de Alternativa' }
        ];

        for (const field of letterFields) {
            const val = document.getElementById(field.id).value.trim();
            if (!validate(val, 'letras')) {
                showToast('Erro de Validação', `${field.name} deve conter apenas letras.`, 'error');
                return;
            }
        }

        // Envio para Netlify Function
        try {
            const btnSubmit = document.getElementById('btn-submit');
            btnSubmit.disabled = true;
            btnSubmit.innerText = 'Enviando...';

            const response = await fetch('/.netlify/functions/submit-fpl', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                showToast('Sucesso!', 'Plano de voo enviado com sucesso para o Neon.');
                form.reset();
            } else {
                const err = await response.json();
                throw new Error(err.error || 'Falha ao conectar com o servidor');
            }
        } catch (error) {
            console.error(error);
            showToast('Erro no Envio', error.message, 'error');
        } finally {
            const btnSubmit = document.getElementById('btn-submit');
            btnSubmit.disabled = false;
            btnSubmit.innerText = 'Enviar Plano de Voo';
        }
    });

    btnCancel.addEventListener('click', () => {
        if (confirm('Deseja realmente limpar o formulário?')) {
            form.reset();
        }
    });
});
