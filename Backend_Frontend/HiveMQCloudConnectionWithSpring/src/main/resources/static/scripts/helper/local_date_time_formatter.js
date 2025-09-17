
export function shortFormat(date) {
    const formatter = new Intl.DateTimeFormat('pt-BR', {
        dateStyle: 'short',
        timeStyle: 'short',
    });
    
    return formatter.format(date);
}
